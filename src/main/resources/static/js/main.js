$(() => {

  // GLOBAL SETTINGS
  $('select').select2({
    width: '100%',
    theme: 'bootstrap'
  });
  var stompClient = null;

  // USER FORM SCRIPT
  var usernamePage = $('#username-page');
  var username = null;

  var connect = (event) => {
      username = $('#name').val().trim();
      if (username) {
          usernamePage.hide();
          chatPage.show();

          let socket = new SockJS('/ws');
          stompClient = Stomp.over(socket);

          stompClient.connect({}, () => {
            // Subscribe to the Public Topics
            stompClient.subscribe('/user/queue/reply', onHistoryReceived);
            stompClient.subscribe(`/topic/${$('#currency').val()}/message`, onMessageReceived);
            stompClient.subscribe(`/topic/${$('#currency').val()}/status`, onStatusChanged);
            // Tell your username to the server
            stompClient.send("/app/chat.addUser",
              {},
              JSON.stringify({
                sender: {
                  name: username
                },
                content: $('#currency').val(),
                type: 'JOIN'
              }));
            chatHeader.append($('<h2>').text('Chat main currency ' + $('#currency').val()));
          	connectingElement.hide();
          }, (error) => {
            connectingElement.text(`Could not connect to WebSocket server.
              Please refresh this page to try again!`);
            connectingElement.css('color', 'red');
          });
      }
  }

  var chatPage = $('#chat-page');
  var messageForm = $('#messageForm');
  var messageType = $('#message-type');
  var messageInput = $('#message');
  var rateInput = $('#rate');
  var messageCurrencySelect = $('#message-currency');
  var amountInput = $('#amount');
  var messageArea = $('#messageArea');
  var connectingElement = $('.connecting');
  var chatHeader = $('.chat-header');

  var messageTypeChange = (e) => {
    if (messageType.val() === 'CHAT') {
      messageInput.show();
      rateInput.hide();
      amountInput.hide();
      messageCurrencySelect.hide();
    } else {
      messageInput.hide();
      rateInput.show();
      amountInput.show();
      messageCurrencySelect.show();
    }
  }

  var sendMessage = (event) => {
      let chatMessage = {
          sender: {
            name: username
          },
          type: messageType.val()
      };
      if (chatMessage.type === 'CHAT') {
          chatMessage.content = messageInput.val().trim();
      } else {
          chatMessage.sale = {
            amount: amountInput.val(),
            rate: rateInput.val(),
            currencyBuy: messageCurrencySelect.find('select').val()
          };
      }
      if (chatMessage.content || chatMessage.sale) {
          stompClient.send("/app/chat.sendMessage",
            {}, JSON.stringify(chatMessage));
          messageInput.val('');
          rateInput.val('');
          amountInput.val('');
      }
  }

  var onHistoryReceived = (payload) => {
    let messages = JSON.parse(payload.body);
    messageArea.empty();
    messages.forEach(m => onMessageReceived({body: JSON.stringify(m)}));
  }

  var onMessageReceived = (payload) => {
      let message = JSON.parse(payload.body);
      let messageElement = $('<li>');
      messageElement.attr('data-id', message.id);
      if (message.type === 'JOIN') {
          messageElement.addClass('event-message');
          message.content = message.sender.name + ' joined!';
      } else if (message.type === 'LEAVE') {
          messageElement.addClass('event-message');
          message.content = message.sender.name + ' left!';
      } else {
          messageElement.addClass('chat-message');

          let avatarElement = $('<i>')
          avatarElement.text(message.sender.name[0]);
          avatarElement.css('background-color', getAvatarColor(message.sender.name[0]));

          messageElement.append(avatarElement);

          let usernameElement = $('<span>');
          usernameElement.text(message.sender.name);
          messageElement.append(usernameElement);
      }

      if (message.type === 'SALE' || message.type === 'PURCHASE') {
        let div = $('<div class="offer">')
        if (message.sale.status === 'NEW') {
          div.append($('<p>').text(`Amount: ${message.sale.amount} rate: ${message.sale.rate} currency: ${message.sale.currencyBuy}`))
             .append($('<button type="button" class="btn btn-primary">')
               .text(message.type === 'SALE' ? 'Buy' : 'Sell'));
        } else {
          div.text(`Amount: ${message.sale.amount} rate: ${message.sale.rate} currency: ${message.sale.currencyBuy}
                  ${message.type} user
                  ${message.type === 'SALE' ?
                    message.sale.buyer.name :
                    message.sale.seller.name}`)
        }
        messageElement.append(div);
      } else {
        messageElement.append($('<p>').text(message.content));
      }

      messageArea.append(messageElement);
      messageArea.scrollTop(messageArea.prop('scrollHeight'));
  }

  var onStatusChanged = (payload) => {
      let message = JSON.parse(payload.body);
      messageArea.find(`[data-id=${message.id}] .offer`)
                 .text(`Amount: ${message.sale.amount} rate: ${message.sale.rate} currency: ${message.sale.currencyBuy}
                        ${message.type} user
                        ${message.type === 'SALE' ?
                          message.sale.buyer.name :
                          message.sale.seller.name}`)
  }

  var onBuySell = (e) => {
    let id = $(e.target).parent().parent().attr('data-id');
    let chatMessage = {
        id: id
    };
    stompClient.send("/app/chat.buySell",
      {}, JSON.stringify(chatMessage));
  }

  // UTILS
  var getAvatarColor = (messageSender) => {
      let colors = [
          '#2196F3', '#32c787', '#00BCD4', '#ff5652',
          '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
      ];
      let hash = 0;
      for (let i = 0; i < messageSender.length; i++) {
          hash = 31 * hash + messageSender.charCodeAt(i);
      }
      let index = Math.abs(hash % colors.length);
      return colors[index];
  }

  //EVENTS
  $('#sign-in').click(connect);
  $('#send-button').click(sendMessage);
  messageType.on('change', messageTypeChange);
  messageType.trigger('change');
  messageArea.click('.chat-message button', onBuySell);
});

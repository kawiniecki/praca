{
    extend: 'Ext.data.Model',
    fields: [
          'channel',
          'notification',
          {
			name:'product',
			mapping:'channel.channelProduct'
		  }         
    ],
    idProperty:'extid',
    proxy: {
        type: 'rest',
        url : 'rest/subscriptions/products/notifications'
    }
}


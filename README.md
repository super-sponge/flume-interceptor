## flume interceptor

### 演示内容
    本实例演示怎样定制interceptor,达到根据source内容添加head内容目的，该实例可以用户一个数据源需要根据内容向不同sink发送数据。
### 程序运行
    cd ./src/main/script
    ./start-ng.sh
### 程序讲解
        //intercept 处理内容
        public Event intercept(Event event) {
            Map<String, String> headers = event.getHeaders();
    
            if (preserveExisting && headers.containsKey(key)) {
                return event;
            }
            // 此处可以活取event的head，如果在处理文件时候，可以根据文件名处理
            String msg = new String(event.getBody(), charset);
            String ret = msg.substring(0,1);
    
            logger.debug(String.format("TagInterceptor ret=%s",ret));
    
            headers.put(key, ret);
            return event;
        }
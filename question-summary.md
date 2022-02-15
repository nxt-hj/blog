# Rsync SSH

1. Host key verification failed.

    `ssh-keyscan -t rsa domain.com >> ~/.ssh/known_hosts`
    
    如果之前连接过，但是用户名被改变了，需要先执行删除操作
    
    `ssh-keygen -R domain.com`
  
2. Permission denied, please try again.Permission denied, please try again.root@192.168.1.37: Permission denied (publickey,gssapi-keyex,gssapi-with-mic,password).
    
    这是由于自动执行时没有输入密码,连续被拒绝2次后失败，只要记住密码免密登录
    
    `ssh-keygen -t rsa` 一直回车
    
    `ssh-copy-id domain.com` 根据提示输入登录密码，执行完即可
    

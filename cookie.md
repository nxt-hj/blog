## Cookie主要用途

**记录会话**

  我们都知道http为无状态协议，即使客户端C上一次请求A告诉服务端S他是谁，下一次请求B到服务端S时，服务端S依然不知道是谁在访问，所以需要一个记录来跟踪用户登录状态，最常用的就是Cookie了，现在也有很多不传送cookie，走http headers进行认证，这样的话可以减少传送一些客户端才需要使用的cookie，节省带宽

**行为分析**

  通过cookie去跟踪用户的轨迹和使用情况，分析这些这些数据，（用户浏览了什么页面和内容/什么时候访问的/在当前页面停留多久/浏览最多的页面/查询最多的关键词等），从而可以定向精准的投放广告服务或者个性化服务，例如谷歌搜索，会记录你每次的搜索关键词，向你推荐相关关键词，了解你的兴趣爱好

## Cookie的设置
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
**expires** => 值为GMT时间字符表示过期的日期点，可以设置该值为过去日期来删除该cookie<br>
**maxage** => 值为未来的时间长度，单位为S，表示过期的时间长度<br>
**path** => 限制cookie在该路径下，只能在该路径下才能发送<br>
**domain** => 限制cookie在该域下，可以在该域和子域下发送，如果没有设置则只在该域下发送<br>
**secure** => 限制只能在https下加密传输<br>
**samesite** => 控制跨域下请求是否发送cookie,有三个值，分别为none(可以),strict（只在同域下发送站点）,lax（和strict差不多，不过只能通过link这种外部导航方式进入才会发送），默认为lax<br>
**httponly** => 只通过http暴露cookie,这就表示不能通过js脚本document.cookie设置和访问
## 

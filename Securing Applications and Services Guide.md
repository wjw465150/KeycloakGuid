# 保护应用程序和服务指南 {#Securing Applications and Services Guide}



## 1. 概述 {#Overview}

Keycloak支持OpenID Connect（OAuth 2.0的扩展）和SAML 2.0。 在保护客户端和服务时，首先需要确定的是您要使用的两个中的哪一个。 如果您愿意，您也可以选择使用OpenID Connect和其他SAML安全保护。

To secure clients and services you are also going to need an adapter or library for the protocol you’ve selected. Keycloak comes with its own adapters for selected platforms, but it is also possible to use generic OpenID Connect Resource Provider and SAML Service Provider libraries.

### 1.1. 什么是客户端适配器? {#What_are_Client_Adapters}

Keycloak客户端适配器是使用Keycloak轻松保护应用程序和服务的库。 我们将它们称为适配器而不是库，因为它们提供了与底层平台和框架的紧密集成。 这使得我们的适配器易于使用，并且它们需要的库样板代码少于库通常所需的代码。

### 1.2. 支持的平台 {#Supported_Platforms}

#### 1.2.1. OpenID Connect {#OpenID_Connect}

##### Java {#Java}

- [JBoss EAP](https://www.keycloak.org/docs/latest/securing_apps/index.html#_jboss_adapter)
- [WildFly](https://www.keycloak.org/docs/latest/securing_apps/index.html#_jboss_adapter)
- [Fuse](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse_adapter)
- [Tomcat](https://www.keycloak.org/docs/latest/securing_apps/index.html#_tomcat_adapter)
- [Jetty 9](https://www.keycloak.org/docs/latest/securing_apps/index.html#_jetty9_adapter)
- [Servlet Filter](https://www.keycloak.org/docs/latest/securing_apps/index.html#_servlet_filter_adapter)
- [Spring Boot](https://www.keycloak.org/docs/latest/securing_apps/index.html#_spring_boot_adapter)
- [Spring Security](https://www.keycloak.org/docs/latest/securing_apps/index.html#_spring_security_adapter)

##### JavaScript (client-side) {#JavaScript_client_side}

- [JavaScript](https://www.keycloak.org/docs/latest/securing_apps/index.html#_javascript_adapter)

##### Node.js (server-side) {#Node_js_server_side}

- [Node.js](https://www.keycloak.org/docs/latest/securing_apps/index.html#_nodejs_adapter)

#### 1.2.2. C# {#C__}

- [OWIN](https://github.com/dylanplecki/KeycloakOwinAuthentication) (community)

#### 1.2.3. Python {#Python}

- [oidc](https://pypi.org/project/oic/) (generic)

#### 1.2.4. Android {#Android}

- [AppAuth](https://github.com/openid/AppAuth-Android) (generic)
- [AeroGear](https://github.com/aerogear/aerogear-android-authz) (generic)

#### 1.2.5. iOS {#iOS}

- [AppAuth](https://github.com/openid/AppAuth-iOS) (generic)
- [AeroGear](https://github.com/aerogear/aerogear-ios-oauth2) (generic)

##### Apache HTTP Server {#Apache_HTTP_Server}

- [mod_auth_openidc](https://github.com/zmartzone/mod_auth_openidc)

#### 1.2.6. SAML {#SAML}

##### Java {#Java}

- [JBoss EAP](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml_jboss_adapter)
- [WildFly](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml_jboss_adapter)
- [Tomcat](https://www.keycloak.org/docs/latest/securing_apps/index.html#_tomcat_adapter)
- [Jetty](https://www.keycloak.org/docs/latest/securing_apps/index.html#_jetty_saml_adapter)

##### Apache HTTP Server {#Apache_HTTP_Server}

- [mod_auth_mellon](https://www.keycloak.org/docs/latest/securing_apps/index.html#_mod_auth_mellon)

### 1.3. 支持的协议 {#Supported_Protocols}

#### 1.3.1. OpenID连接 {#OpenID_Connect}

[OpenID 连接](https://openid.net/connect/) (OIDC)是一个身份验证协议，它是[OAuth 2.0](https://tools.ietf.org/html/rfc6749)的扩展。虽然OAuth 2.0只是一个构建授权协议的框架，而且主要是不完整的，但OIDC是一个完整的身份验证和授权协议。 OIDC还大量使用了[Json Web Token](https://jwt.io/) (JWT)标准集。这些标准定义了一种身份令牌JSON格式，以及以一种紧凑且web友好的方式对数据进行数字签名和加密的方法。

使用OIDC时，实际上有两种用例。 第一个是要求Keycloak服务器为用户验证用户的应用程序。 成功登录后，应用程序将收到*identity token(身份令牌)*和*access token(访问令牌)*。 *身份令牌*包含有关用户的信息，例如用户名，电子邮件和其他个人资料信息。 *访问令牌*由领域进行数字签名，并包含访问信息（如用户角色映射），应用程序可以使用该信息来确定允许用户在应用程序上访问哪些资源。

第二种用例是希望获得远程服务访问权限的客户端。 在这种情况下，客户端要求Keycloak获取*访问令牌*，它可以代表用户在其他远程服务上调用。 Keycloak对用户进行身份验证，然后要求用户同意授予访问请求它的客户端的权限。 然后客户端接收*访问令牌*。 此*访问令牌*由领域进行数字签名。 客户端可以使用此*访问令牌*在远程服务上进行REST调用。 REST服务提取*访问令牌*，验证令牌的签名，然后根据令牌内的访问信息决定是否处理请求。

#### 1.3.2. SAML 2.0 {#SAML_2_0}

[SAML 2.0](http://saml.xml.org/saml-specifications) 是与OIDC类似的规范，但是更老，更成熟。 它的根源在于SOAP和过多的WS-*规范，所以它往往比OIDC更冗长。 SAML 2.0主要是一种身份验证协议，通过在身份验证服务器和应用程序之间交换XML文档来工作。 XML签名和加密用于验证请求和响应。

在Keycloak中，SAML提供两种用例：浏览器应用程序和REST调用。

使用SAML时，实际上有两种用例。 第一个是要求Keycloak服务器为用户验证用户的应用程序。 成功登录后，应用程序将收到一个XML文档，其中包含称为SAML断言的内容，该断言指定了有关用户的各种属性。 此XML文档由领域进行数字签名，并包含访问信息（如用户角色映射），应用程序可以使用该信息来确定允许用户在应用程序上访问哪些资源。

第二种用例是希望获得远程服务访问权限的客户端。 在这种情况下，客户端要求Keycloak获取可用于代表用户在其他远程服务上调用的SAML断言。

#### 1.3.3. OpenID Connect 与 SAML {#OpenID_Connect_vs_SAML}

在OpenID Connect和SAML之间进行选择不仅仅是使用更新的协议（OIDC）而不是旧的更成熟的协议（SAML）。

在大多数情况下，Keycloak建议使用OIDC。

SAML往往比OIDC更冗长。

除了交换数据的详细程度之外，如果您比较规范，您会发现OIDC旨在与Web一起工作，同时SAML被改装为在Web上运行。 例如，OIDC也更适合HTML5/JavaScript应用程序，因为它比SAML更容易在客户端实现。 由于令牌采用JSON格式，因此JavaScript更易于使用。 您还将找到一些很好的功能，可以更轻松地在Web应用程序中实现安全性。 例如，查看规范用于轻松确定用户是否仍在登录的[iframe技巧](https://openid.net/specs/openid-connect-session-1_0.html#ChangeNotification)。

SAML虽然有它的用途。 正如您所看到的，OIDC规范的发展，您会发现它们实现了SAML多年来所拥有的越来越多的功能。 我们经常看到人们选择SAML而不是OIDC，因为人们认为它更成熟，也因为他们已经有了现有的应用程序。

## 2. OpenID 连接器 {#OpenID_Connect}

本节介绍如何使用Keycloak适配器或通用OpenID Connect资源提供程序库通过OpenID Connect保护应用程序和服务。

### 2.1. Java适配器 {#Java_Adapters}

Keycloak为Java应用程序提供了一系列不同的适配器。 选择正确的适配器取决于目标平台。

所有Java适配器共享[Java Adapters Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_java_adapter_config) 章节中描述的一组常用配置选项。

#### 2.1.1. Java适配器配置 {#Java_Adapter_Config}

Keycloak支持的每个Java适配器都可以通过简单的JSON文件进行配置。 这可能是这样的：

```json
{
  "realm" : "demo",
  "resource" : "customer-portal",
  "realm-public-key" : "MIGfMA0GCSqGSIb3D...31LwIDAQAB",
  "auth-server-url" : "https://localhost:8443/auth",
  "ssl-required" : "external",
  "use-resource-role-mappings" : false,
  "enable-cors" : true,
  "cors-max-age" : 1000,
  "cors-allowed-methods" : "POST, PUT, DELETE, GET",
  "cors-exposed-headers" : "WWW-Authenticate, My-custom-exposed-Header",
  "bearer-only" : false,
  "enable-basic-auth" : false,
  "expose-token" : true,
  "verify-token-audience" : true,
   "credentials" : {
      "secret" : "234234-234234-234234"
   },

   "connection-pool-size" : 20,
   "disable-trust-manager": false,
   "allow-any-hostname" : false,
   "truststore" : "path/to/truststore.jks",
   "truststore-password" : "geheim",
   "client-keystore" : "path/to/client-keystore.jks",
   "client-keystore-password" : "geheim",
   "client-key-password" : "geheim",
   "token-minimum-time-to-live" : 10,
   "min-time-between-jwks-requests" : 10,
   "public-key-cache-ttl": 86400,
   "redirect-rewrite-rules" : {
   "^/wsmaster/api/(.*)$" : "/api/$1"
   }
}
```

您可以使用`${…}`来替换系统属性。例如`${jboss.server.config.dir}`将替换为`/path/to/Keycloak`。环境变量的替换也通过`env`前缀得到支持，例如。 `${env.MY_ENVIRONMENT_VARIABLE}`。

可以从管理控制台获取初始配置文件。 这可以通过打开管理控制台，从菜单中选择`Clients`并单击相应的客户端来完成。 打开客户端页面后，单击`Installation`选项卡，然后选择`Keycloak OIDC JSON`。

以下是每个配置选项的说明：

- realm

  领域的名称。 这是*必需的*

- resource

  应用程序的客户端ID。 每个应用程序都有一个client-id，用于标识应用程序。 这是*必需的*

- realm-public-key

  领域公钥的PEM格式。 您可以从管理控制台获取此信息。 这是*可选的*，不建议设置它。 如果没有设置，适配器将从Keycloak下载它，它将在需要时总是重新下载（例如Keycloak旋转它的键）。 但是，如果设置了realm-public-key，那么适配器永远不会从Keycloak下载新密钥，所以当Keycloak旋转它的密钥时，适配器将会中断。

- auth-server-url

  Keycloak服务器的基本URL。 所有其他Keycloak页面和REST服务端点都源于此。 它的形式通常为`https://host:port/auth`。 这是*必需的*

- ssl-required

  确保与Keycloak服务器之间的所有通信均通过HTTPS进行。 在生产中，这应该设置为“all”。 这是*可选的*。 默认值为*external*表示默认情况下HTTPS是外部请求所必需的。 有效值为“all”，“external”和“none”。

- confidential-port

  Keycloak服务器用于通过SSL/TLS进行安全连接的机密端口。 这是*可选的*。 默认值为*8443*。

- use-resource-role-mappings

  如果设置为true，那么适配器将在令牌内查找用户的应用程序级别角色映射。 如果为false，它将查看用户角色映射的领域级别。 这是*可选的*。 默认值为* false*。

- public-client

  如果设置为true，则适配器将不会将客户端的凭据发送到Keycloak。 这是*可选的*。 默认值为*false*。

- enable-cors

  这使CORS支持成为可能。 它将处理CORS预检请求。 它还将查看访问令牌以确定有效的来源。 这是*可选的*。 默认值为*false*。

- cors-max-age

  如果启用了CORS，则设置`Access-Control-Max-Age`标头的值。 这是*可选的*。 如果未设置，则在CORS响应中不返回此标头。

- cors-allowed-methods

  如果启用了CORS，则设置`Access-Control-Allow-Methods`标头的值。 这应该是逗号分隔的字符串。 这是*可选的*。 如果未设置，则在CORS响应中不返回此标头。

- cors-allowed-headers

  如果启用了CORS，则设置`Access-Control-Allow-Headers`标头的值。 这应该是逗号分隔的字符串。 这是*可选的*。 如果未设置，则在CORS响应中不返回此标头。

- cors-exposed-headers

  如果启用了CORS，则设置`Access-Control-Expose-Headers`标头的值。 这应该是逗号分隔的字符串。 这是*可选的*。 如果未设置，则在CORS响应中不返回此标头。

- bearer-only

  对于服务，应将其设置为*true*。 如果启用，适配器将不会尝试对用户进行身份验证，而只会验证承载令牌。 这是*可选的*。 默认值为*false*。

- autodetect-bearer-only

  如果您的应用程序同时提供Web应用程序和Web服务（例如SOAP或REST），则应将其设置为*true*。 它允许您将未经身份验证的Web应用程序用户重定向到Keycloak登录页面，但是将HTTP`401`状态代码发送给未经身份验证的SOAP或REST客户端，因为他们无法理解重定向到登录页面。 Keycloak基于典型的标题自动检测SOAP或REST客户端，如`X-Requested-With`，`SOAPAction` 或 `Accept`。 默认值为*false*。

- enable-basic-auth

  这告诉适配器也支持基本身份验证。 如果启用此选项，则还必须提供*secret*。 这是*选的*。 默认值为*false*。

- expose-token

  如果是`true`，则经过身份验证的浏览器客户端（通过JavaScript HTTP调用）可以通过URL `root/k_query_bearer_token`获取签名访问令牌。 这是*可选的*。 默认值为*false*。

- credentials

  指定应用程序的凭据。 这是一个对象表示法，其中键是凭证类型，值是凭证类型的值。 目前支持密码和jwt。 这是*必须的*仅适用于具有'Confidential(机密)'访问类型的客户。

- connection-pool-size

  适配器将对Keycloak服务器进行单独的HTTP调用，以将访问代码转换为访问令牌。 此配置选项定义应该合并到Keycloak服务器的连接数。 这是*可选的*。 默认值为`20`。

- disable-trust-manager

  如果Keycloak服务器需要HTTPS并且此配置选项设置为true，则不必指定信任库。 此设置仅应在开发期间使用，**永远**不要在生产中使用，因为它将禁用SSL证书的验证。 这是*可选的*。 默认值为`false`。

- allow-any-hostname

  如果Keycloak服务器需要HTTPS并且此配置选项设置为`true`，则通过信任库验证Keycloak服务器的证书，但不会进行主机名验证。 此设置仅应在开发期间使用，**永远**不要在生产中使用，因为它将禁用SSL证书的验证。 此测试在测试环境中可能很有用。这是*可选的*。 默认值为`false`。

- proxy-url

  HTTP代理的URL（如果使用）。

- truststore

  该值是信任库文件的文件路径。 如果在路径前加上`classpath:`，那么将从部署的类路径中获取信任库。 用于与Keycloak服务器的传出HTTPS通信。 发出HTTPS请求的客户端需要一种方法来验证他们正在与之通信的服务器的主机。 这就是委托人所做的。 密钥库包含一个或多个可信主机证书或证书颁发机构。 您可以通过提取Keycloak服务器的SSL密钥库的公共证书来创建此信任库。 这是* 必须的*，除非`ssl-required`是`none`或`disable-trust-manager` 是 `true`。

- truststore-password

  信任库的密码。 如果设置了`truststore`并且信任库需要密码，那么这是*必须的*。

- client-keystore

  这是密钥库文件的文件路径。 当适配器向Keycloak服务器发出HTTPS请求时，此密钥库包含双向SSL的客户端证书。 这是*可选的*。

- client-keystore-password

  客户端密钥库的密码。 如果设置了`client-keystore`，这是*必须的*。

- client-key-password

  客户密钥的密码。 如果设置了`client-keystore`，这是*必须的*。

- always-refresh-token

  如果*true*，则适配器将在每个请求中刷新令牌。

- register-node-at-startup

  如果*true*，则适配器将向Keycloak发送注册请求。 它默认为*false*，仅在应用程序集群时才有用。 有关详细信息，请参阅[应用程序群集](https://www.keycloak.org/docs/latest/securing_apps/index.html#_applicationclustering)

- register-node-period

  重新注册适配器到Keycloak的期限。 应用程序集群时很有用。 有关详细信息，请参阅[应用程序群集](https://www.keycloak.org/docs/latest/securing_apps/index.html#_applicationclustering)

- token-store

  可能的值是*session*和*cookie*。 默认为*session*，这意味着适配器在HTTP会话中存储帐户信息。 如果是*cookie*表示在cookie中存储信息。 有关详细信息，请参阅[应用程序群集](https://www.keycloak.org/docs/latest/securing_apps/index.html#_applicationclustering)

- token-cookie-path

  使用cookie存储时，此选项设置用于存储帐户信息的cookie的路径。 如果它是相对路径，则假定应用程序在上下文根中运行，并且相对于该上下文根进行解释。 如果它是绝对路径，则绝对路径用于设置cookie路径。 默认使用相对于上下文根的路径。

- principal-attribute

  使用OpenID Connect ID Token属性填充UserPrincipal名称。 如果token属性为null，则默认为`sub`。 可能的值是`sub`，`preferred_username`，`email`，`name`，`nickname`，`given_name`，`family_name`。

- turn-off-change-session-id-on-login

  默认情况下，在某些平台上成功登录时会更改会话ID以插入安全攻击向量。 如果要将其关闭，请将此更改为true这是*可选的*。 默认值为*false*。

- token-minimum-time-to-live

  在Keycloak服务器到期之前使用Keycloak服务器抢先刷新活动访问令牌的时间（以秒为单位）。 当访问令牌被发送到另一个REST客户端时，这在它可能在评估之前到期时特别有用。 该值不应超过领域的访问令牌寿命。 这是*可选的*。 默认值为`0`秒，因此适配器将刷新访问令牌，如果它已过期。

- min-time-between-jwks-requests

  指定Keycloak检索新公钥的两个请求之间的最小间隔的时间量（以秒为单位）。 默认为10秒。 当适配器识别带有未知`kid`的令牌时，它总是会尝试下载新的公钥。 但是，它不会每10秒尝试一次（默认情况下）。 这是为了避免DoS，当攻击者发送大量带有错误`kid`强制适配器的令牌向Keycloak发送大量请求时。

- public-key-cache-ttl

  指定Keycloak检索新公钥的两个请求之间的最大间隔的时间量（以秒为单位）。 默认为86400秒（1天）。 当适配器识别带有未知`kid`的令牌时，它总是会尝试下载新的公钥。 如果它识别已知`kid`的令牌，它将只使用先前下载的公钥。 但是，每个此配置的间隔（默认为1天）至少一次将是新的公钥，即使令牌的`kid`已知，也会一直下载。

- ignore-oauth-query-parameter

  默认为`false`，如果设置为`true`将关闭处理承载令牌处理的`access_token`查询参数。 如果用户只传入`access_token`，他们将无法进行身份验证

- redirect-rewrite-rules

  如果需要，请指定重定向URI重写规则。 这是一个对象表示法，其中键是要与Redirect URI匹配的正则表达式，值是替换String。 `$`字符可用于替换String中的反向引用。

- verify-token-audience

  如果设置为“true”，则在使用承载令牌进行身份验证期间，适配器将验证令牌是否包含此客户端名称（资源）作为受众。 该选项对于主要服务于由承载令牌验证的请求的服务特别有用。 默认设置为`false`，但为了提高安全性，建议启用此功能。 有关受众群体支持的详细信息，请参阅[受众群体支持](https://www.keycloak.org/docs/6.0/server_admin/#_audience)。

#### 2.1.2. JBoss EAP/WildFly 适配器 {#JBoss_EAP_WildFly_Adapter}

为了能够保护部署在JBoss EAP，WildFly或JBoss AS上的WAR应用程序，您必须安装和配置Keycloak适配器子系统。 然后，您有两个选项来保护您的WAR。

您可以在WAR中提供适配器配置文件，并在web.xml中将auth-method更改为KEYCLOAK。

或者，您根本不必修改WAR，并且可以通过配置文件中的Keycloak适配器子系统配置（例如`standalone.xml`）来保护它。 本节将介绍这两种方法。

##### 安装适配器 {#Installing_the_adapter}

根据您使用的服务器版本，适配器可作为单独的存档提供。

> 我们只测试和维护适配器，并在发布时提供最新版本的WildFly。 一旦发布了新版本的WildFly，当前的适配器将被弃用，并且在下一个WildFly版本发布后将删除对它们的支持。 另一种方法是将应用程序从WildFly切换到JBoss EAP，因为JBoss EAP适配器的支持时间更长。

安装WildFly 9或更新版本:

```bash
$ cd $WILDFLY_HOME
$ unzip keycloak-wildfly-adapter-dist-6.0.1.zip
```

安装WildFly 8:

```bash
$ cd $WILDFLY_HOME
$ unzip keycloak-wf8-adapter-dist-6.0.1.zip
```

安装JBoss EAP 7:

```bash
$ cd $EAP_HOME
$ unzip keycloak-eap7-adapter-dist-6.0.1.zip
```

安装JBoss EAP 6:

```bash
$ cd $EAP_HOME
$ unzip keycloak-eap6-adapter-dist-6.0.1.zip
```

安装JBoss AS 7.1:

```bash
$ cd $JBOSS_HOME
$ unzip keycloak-as7-adapter-dist-6.0.1.zip
```

此ZIP存档包含特定于Keycloak适配器的JBoss模块。 它还包含JBoss CLI脚本以配置适配器子系统。

要在服务器未运行时配置适配器子系统，请执行：

> 或者，您可以在从命令行安装适配器时指定`server.config`属性，以使用其他配置安装适配器，例如：`-Dserver.config=standalone-ha.xml`。

WildFly 11 或者 更新版本

```bash
$ ./bin/jboss-cli.sh --file=bin/adapter-elytron-install-offline.cli
```

WildFly 10 或者 更旧版本

```bash
$ ./bin/jboss-cli.sh --file=bin/adapter-install-offline.cli
```

> 可以在WildFly 11或更新版本上使用传统的非Elytron适配器，这意味着即使在这些版本上也可以使用`adapter-install-offline.cli`。 但是，我们建议使用较新的Elytron适配器。

或者，如果服务器正在运行，则执行：

WildFly 11 或者 更新版本

```bash
$ ./bin/jboss-cli.sh -c --file=bin/adapter-elytron-install.cli
```

WildFly 10 或者 更旧版本

```bash
$ ./bin/jboss-cli.sh -c --file=bin/adapter-install.cli
```

##### JBoss SSO

WildFly内置支持部署到同一WildFly实例的Web应用程序的单点登录。 使用Keycloak时不应启用此功能。

##### 每个WAR配置必需

本节介绍如何通过在WAR包中添加配置和编辑文件来直接保护WAR。

您必须做的第一件事是在WAR的`WEB-INF`目录中创建一个`keycloak.json`适配器配置文件。

此配置文件的格式在[Java适配器配置](https://www.keycloak.org/docs/latest/securing_apps/index.html#java_adapter_config) 部分中进行了描述。

接下来，您必须在`web.xml`中将`auth-method`设置为`KEYCLOAK`。 您还必须使用标准servlet安全性来指定URL上的角色基础约束。

这是一个例子：

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">

    <module-name>application</module-name>

    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Admins</web-resource-name>
            <url-pattern>/admin/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>admin</role-name>
        </auth-constraint>
        <user-data-constraint>
            <transport-guarantee>CONFIDENTIAL</transport-guarantee>
        </user-data-constraint>
    </security-constraint>
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Customers</web-resource-name>
            <url-pattern>/customers/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>user</role-name>
        </auth-constraint>
        <user-data-constraint>
            <transport-guarantee>CONFIDENTIAL</transport-guarantee>
        </user-data-constraint>
    </security-constraint>

    <login-config>
        <auth-method>KEYCLOAK</auth-method>
        <realm-name>this is ignored currently</realm-name>
    </login-config>

    <security-role>
        <role-name>admin</role-name>
    </security-role>
    <security-role>
        <role-name>user</role-name>
    </security-role>
</web-app>
```

##### 通过适配器子系统保护WAR

您不必修改WAR以使用Keycloak保护它。 相反，您可以通过Keycloak适配器子系统从外部保护它。 虽然您不必将KEYCLOAK指定为`auth-method`，但仍需要在`web.xml`中定义`security-constraints`。 但是，您不必创建`WEB-INF/keycloak.json`文件。 而是在Keycloak子系统定义中的服务器配置（即`standalone.xml`）中定义此元数据。

```xml
<extensions>
  <extension module="org.keycloak.keycloak-adapter-subsystem"/>
</extensions>

<profile>
  <subsystem xmlns="urn:jboss:domain:keycloak:1.1">
     <secure-deployment name="WAR MODULE NAME.war">
        <realm>demo</realm>
        <auth-server-url>http://localhost:8081/auth</auth-server-url>
        <ssl-required>external</ssl-required>
        <resource>customer-portal</resource>
        <credential name="secret">password</credential>
     </secure-deployment>
  </subsystem>
</profile>
```

`secure-deployment``name`属性标识要保护的WAR。 它的值是`web.xml`中定义的`module-name`，附加了`.war`。 其余的配置几乎与[Java adapter configuration](https://www.keycloak.org/docs/latest/securing_apps/index.html#_java_adapter_config)中定义的`keycloak.json`配置选项一一对应。

例外是`credential`元素。

为了方便您，您可以转到Keycloak管理控制台并转到此WAR所对应的应用程序的客户端/安装选项卡。 它提供了一个可以剪切和粘贴的示例XML文件。

如果您有多个由同一领域保护的部署，则可以在单独的元素中共享领域配置。 例如：

```xml
<subsystem xmlns="urn:jboss:domain:keycloak:1.1">
    <realm name="demo">
        <auth-server-url>http://localhost:8080/auth</auth-server-url>
        <ssl-required>external</ssl-required>
    </realm>
    <secure-deployment name="customer-portal.war">
        <realm>demo</realm>
        <resource>customer-portal</resource>
        <credential name="secret">password</credential>
    </secure-deployment>
    <secure-deployment name="product-portal.war">
        <realm>demo</realm>
        <resource>product-portal</resource>
        <credential name="secret">password</credential>
    </secure-deployment>
    <secure-deployment name="database.war">
        <realm>demo</realm>
        <resource>database-service</resource>
        <bearer-only>true</bearer-only>
    </secure-deployment>
</subsystem>
```

##### 安全域

要将安全上下文传播到EJB层，您需要将其配置为使用“keycloak”安全域。 这可以通过@SecurityDomain注释来实现：

```java
import org.jboss.ejb3.annotation.SecurityDomain;
...

@Stateless
@SecurityDomain("keycloak")
public class CustomerService {

    @RolesAllowed("user")
    public List<String> getCustomers() {
        return db.getCustomers();
    }
}
```

#### 2.1.3. 从RPM安装JBoss EAP适配器 {#Installing_JBoss_EAP_Adapter_from_an_RPM}

从RPM安装EAP 7适配器：

> 在Red Hat Enterprise Linux 7中，`term channel`被替换为`term repository`。 在这些说明中，仅使用`term repository`。

您必须先订阅JBoss EAP 7.2存储库，然后才能从RPM安装EAP 7适配器。

先决条件

1. 确保您的Red Hat Enterprise Linux系统已使用Red Hat Subscription Manager注册到您的帐户。 有关更多信息，请参阅[Red Hat订阅管理文档](https://access.redhat.com/documentation/en-us/red_hat_subscription_management/1/html-single/quick_registration_for_rhel/index)。
2. 如果您已经订阅了另一个JBoss EAP存储库，则必须先取消订阅该存储库。

使用Red Hat订阅管理器，使用以下命令订阅JBoss EAP 7.2存储库。根据您的Red Hat Enterprise Linux版本，将<rhel_version>替换为6或7。</rhel_version>

```bash
$ sudo subscription-manager repos --enable=jb-eap-7-for-rhel-<RHEL_VERSION>-server-rpms
```

使用以下命令安装OIDC的EAP 7适配器:

```bash
$ sudo yum install eap7-keycloak-adapter-sso7_3
```

> RPM安装的默认EAP_HOME路径是:/opt/rh/eap7/root/usr/share/wildfly。

运行适当的模块安装脚本。

对于OIDC模块，输入以下命令:

```bash
$ $EAP_HOME/bin/jboss-cli.sh -c --file=$EAP_HOME/bin/adapter-install.cli
```

安装完成。

从RPM安装EAP 6适配器:

> 在Red Hat Enterprise Linux 7中，`term channel`被`term repository`替换。在这些指令中，只使用`term repository`。

在从RPM安装EAP 6适配器之前，必须订阅JBoss EAP 6.0存储库。

先决条件

1. 确保您的Red Hat Enterprise Linux系统使用Red Hat Subscription Manager注册到您的帐户。有关更多信息，请参见[Red Hat订阅管理文档](https://access.redhat.com/documentation/en-us/red_hat_subscription_management/1/html-single/quick_registration_for_rhel/index)。
2. 如果您已经订阅了另一个JBoss EAP存储库，则必须首先从该存储库取消订阅。

使用Red Hat订阅管理器，使用以下命令订阅JBoss EAP 6.0存储库。根据您的Red Hat Enterprise Linux版本，将<rhel_version>替换为6或7。</rhel_version>

```bash
$ sudo subscription-manager repos --enable=jb-eap-6-for-rhel-<RHEL_VERSION>-server-rpms
```

使用以下命令安装OIDC的EAP 6适配器:

```bash
$ sudo yum install keycloak-adapter-sso7_3-eap6
```

> RPM安装的默认EAP_HOME路径是/opt/rh/eap6/root/usr/share/wildfly。 

运行适当的模块安装脚本。

对于OIDC模块，输入以下命令:

```bash
$ $EAP_HOME/bin/jboss-cli.sh -c --file=$EAP_HOME/bin/adapter-install.cli
```

安装完成。

#### 2.1.4. JBoss Fuse 6 适配器 {#JBoss_Fuse_6_Adapter}

Keycloak支持保护在[JBoss Fuse 6](https://developers.redhat.com/products/fuse/overview/)中运行的web应用程序。

JBoss Fuse 6利用[Jetty 9适配器](https://www.keycloak.org/docs/latest/securing_apps/index.html#_jetty9_adapter)作为JBoss Fuse 6.3.0 Rollup 5与[Jetty 9.2服务器](http://www.eclipse.org/jetty/)捆绑在一起和Jetty用于运行各种Web应用程序。

> Fuse 6唯一受支持的版本是最新版本。 如果使用早期版本的Fuse 6，则某些功能可能无法正常工作。 特别是，[Hawtio](https://hawt.io/)集成不适用于早期版本的Fuse 6。

Fuse支持以下项目的安全性:

- 使用Pax Web WAR Extender在Fuse上部署经典的WAR应用程序
- 使用Pax Web白板扩展器将servlet作为OSGI服务部署在Fuse上
- [Apache Camel](http://camel.apache.org/)使用[Camel Jetty](http://camel.apache.org/jetty.html)组件运行的Jetty端点
- [Apache CXF](http://cxf.apache.org/)端点在自己独立的[Jetty引擎](http://cxf.apache.org/docs/jetty-configuration.html)上运行
- [Apache CXF](http://cxf.apache.org/)在CXF servlet提供的默认引擎上运行的端点
- SSH和JMX管理员访问权限
- [Hawtio管理控制台](https://hawt.io/)

##### 在Fuse 6中保护Web应用程序 {#Securing_Your_Web_Applications_Inside_Fuse_6}

您必须先安装Keycloak Karaf功能。 接下来，您需要根据要保护的应用程序类型执行这些步骤。 所有引用的Web应用程序都需要将Keycloak Jetty身份验证器注入底层Jetty服务器。 实现此目标的步骤取决于应用程序类型。 细节描述如下。

最好的起点是看看作为`fuse`目录中Keycloak示例的一部分捆绑的Fuse演示。 通过测试和理解演示，大多数步骤都应该是可以理解的。

##### 安装Keycloak功能 {#Installing_the_Keycloak_Feature}

您必须首先在JBoss Fuse环境中安装`keycloak`功能。 keycloak功能包括Fuse适配器和所有第三方依赖项。 您可以从Maven存储库或存档中安装它。

###### 从Maven存储库安装 {#Installing_from_the_Maven_Repository}

作为先决条件，您必须在线并且可以访问Maven存储库。

对于社区来说，只要在maven中央存储库中提供所有工件和第三方依赖项就足够了。

要使用Maven存储库安装keycloak功能，请完成以下步骤：

1. 启动JBoss Fuse 6.3.0 Rollup 5; 然后在Karaf终端类型：

   ```
   features:addurl mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
   features:install keycloak
   ```

2. 您可能还需要安装Jetty 9功能：

   ```
   features:install keycloak-jetty9-adapter
   ```

3. 确保已安装功能：

   ```
   features:list | grep keycloak
   ```

###### 从ZIP捆绑包安装 {#Installing_from_the_ZIP_bundle}

如果您处于脱机状态或不想使用Maven获取JAR文件和其他工件，这将非常有用。

要从ZIP存档安装Fuse适配器，请完成以下步骤：

1. 下载Keycloak Fuse适配器ZIP存档文件。

2. 将其解压缩到JBoss Fuse的根目录中。 然后将依赖项安装在`system`目录下。 您可以覆盖所有现有的jar文件。

   将此用于JBoss Fuse 6.3.0汇总5：

   ```bash
   cd /path-to-fuse/jboss-fuse-6.3.0.redhat-254
   unzip -q /path-to-adapter-zip/keycloak-fuse-adapter-6.0.1.zip
   ```

3. 启动Fuse并在Fuse/karaf终端中运行以下命令:

   ```bash
   features:addurl mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
   features:install keycloak
   ```

4. 安装相应的Jetty适配器。 由于artifacts可直接在JBoss Fuse`system`目录中使用，因此您无需使用Maven存储库。

##### 保护经典WAR应用程序 {#Securing_a_Classic_WAR_Application}

保护WAR应用程序所需的步骤如下：

1. 在`/WEB-INF/web.xml`文件中，声明必要的：

   - <security-constraint>元素中的安全性约束

   - <login-config>元素中的登录配置

   - <security-role>元素中的安全角色。

     例如：

   ```xml
     <?xml version="1.0" encoding="UTF-8"?>
     <web-app xmlns="http://java.sun.com/xml/ns/javaee"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
              version="3.0">
     
         <module-name>customer-portal</module-name>
     
         <welcome-file-list>
             <welcome-file>index.html</welcome-file>
         </welcome-file-list>
     
         <security-constraint>
             <web-resource-collection>
                 <web-resource-name>Customers</web-resource-name>
                 <url-pattern>/customers/*</url-pattern>
             </web-resource-collection>
             <auth-constraint>
                 <role-name>user</role-name>
             </auth-constraint>
         </security-constraint>
     
         <login-config>
             <auth-method>BASIC</auth-method>
             <realm-name>does-not-matter</realm-name>
         </login-config>
     
         <security-role>
             <role-name>admin</role-name>
         </security-role>
         <security-role>
             <role-name>user</role-name>
         </security-role>
     </web-app>
   ```

2. 将带有验证器的`jetty-web.xml`文件添加到`/WEB-INF/jetty-web.xml`文件中。

   例如：

   ```xml
   <?xml version="1.0"?>
   <!DOCTYPE Configure PUBLIC "-//Mort Bay Consulting//DTD Configure//EN"
    "http://www.eclipse.org/jetty/configure_9_0.dtd">
   <Configure class="org.eclipse.jetty.webapp.WebAppContext">
       <Get name="securityHandler">
           <Set name="authenticator">
               <New class="org.keycloak.adapters.jetty.KeycloakJettyAuthenticator">
               </New>
           </Set>
       </Get>
   </Configure>
   ```

3. 在WAR的`/WEB-INF/`目录中，创建一个新文件keycloak.json。 该配置文件的格式在[Java Adapters Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_java_adapter_config) 部分中进行了描述。 也可以按[配置外部适配器](https://www.keycloak.org/docs/latest/securing_apps/index.html#config_external_adapter)中的说明在外部使用此文件。

4. 确保您的WAR应用程序在`Import-Package`标题下的`META-INF/MANIFEST.MF`文件中导入`org.keycloak.adapters.jetty`和更多软件包。 在项目中使用`maven-bundle-plugin`可以在清单中正确生成OSGI头文件。 请注意，包的“*”解析不会导入`org.keycloak.adapters.jetty`包，因为它不是由应用程序或Blueprint或Spring描述符使用，而是在`jetty-web.xml`文件中使用。

   要导入的包的列表可能是这样的:

   ```
   org.keycloak.adapters.jetty;version="6.0.1",
   org.keycloak.adapters;version="6.0.1",
   org.keycloak.constants;version="6.0.1",
   org.keycloak.util;version="6.0.1",
   org.keycloak.*;version="6.0.1",
   *;resolution:=optional
   ```

###### 配置外部适配器 {#Configuring_the_External_Adapter}

如果您不希望将`keycloak.json`适配器配置文件捆绑在WAR应用程序中，而是根据命名约定在外部提供并加载，请使用此配置方法。

要启用该功能，请将此部分添加到`/WEB_INF/web.xml`文件中：

```xml
<context-param>
    <param-name>keycloak.config.resolver</param-name>
    <param-value>org.keycloak.adapters.osgi.PathBasedKeycloakConfigResolver</param-value>
</context-param>
```

该组件使用`keycloak.config`或`karaf.etc` java属性来搜索基本文件夹以找到配置。 然后在其中一个文件夹中搜索名为`<your_web_context> -keycloak.json`的文件。

因此，例如，如果您的Web应用程序具有上下文`my-portal`，那么您的适配器配置将从`$FUSE_HOME/etc/my-portal-keycloak.json`文件加载。

##### 保护部署为OSGI服务的Servlet {#Securing_a_Servlet_Deployed_as_an_OSGI_Service}

如果在OSGI捆绑项目中有一个未部署为经典WAR应用程序的servlet类，则可以使用此方法。 Fuse使用Pax Web Whiteboard Extender将这些servlet部署为Web应用程序。

要使用Keycloak保护您的servlet，请完成以下步骤：

1. Keycloak提供了PaxWebIntegrationService，它允许注入jetty-web.xml并为您的应用程序配置安全性约束。 您需要在应用程序内的`OSGI-INF/blueprint/blueprint.xml`文件中声明此类服务。 请注意，您的servlet需要依赖它。 配置示例：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://www.osgi.org/xmlns/blueprint/v1.0.0
              http://www.osgi.org/xmlns/blueprint/v1.0.0/blueprint.xsd">
   
       <!-- Using jetty bean just for the compatibility with other fuse services -->
       <bean id="servletConstraintMapping" class="org.eclipse.jetty.security.ConstraintMapping">
           <property name="constraint">
               <bean class="org.eclipse.jetty.util.security.Constraint">
                   <property name="name" value="cst1"/>
                   <property name="roles">
                       <list>
                           <value>user</value>
                       </list>
                   </property>
                   <property name="authenticate" value="true"/>
                   <property name="dataConstraint" value="0"/>
               </bean>
           </property>
           <property name="pathSpec" value="/product-portal/*"/>
       </bean>
   
       <bean id="keycloakPaxWebIntegration" class="org.keycloak.adapters.osgi.PaxWebIntegrationService"
             init-method="start" destroy-method="stop">
           <property name="jettyWebXmlLocation" value="/WEB-INF/jetty-web.xml" />
           <property name="bundleContext" ref="blueprintBundleContext" />
           <property name="constraintMappings">
               <list>
                   <ref component-id="servletConstraintMapping" />
               </list>
           </property>
       </bean>
   
       <bean id="productServlet" class="org.keycloak.example.ProductPortalServlet" depends-on="keycloakPaxWebIntegration">
       </bean>
   
       <service ref="productServlet" interface="javax.servlet.Servlet">
           <service-properties>
               <entry key="alias" value="/product-portal" />
               <entry key="servlet-name" value="ProductServlet" />
               <entry key="keycloak.config.file" value="/keycloak.json" />
           </service-properties>
       </service>
   
   </blueprint>
   ```

   - 您可能需要在项目中包含`WEB-INF`目录（即使您的项目不是Web应用程序）并创建`/WEB-INF/jetty-web.xml`和`/WEB-INF/keycloak.json`文件，如[经典WAR应用程序](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse_adapter_classic_war)部分。 请注意，您不需要`web.xml`文件，因为在蓝图配置文件中声明了安全性约束。

2. `META-INF/MANIFEST.MF`中的`Import-Package`必须至少包含以下导入：

   ```properties
   org.keycloak.adapters.jetty;version="6.0.1",
   org.keycloak.adapters;version="6.0.1",
   org.keycloak.constants;version="6.0.1",
   org.keycloak.util;version="6.0.1",
   org.keycloak.*;version="6.0.1",
   *;resolution:=optional
   ```

##### 保护Apache Camel应用程序 {#Securing_an_Apache_Camel_Application}

您可以通过添加带有`KeycloakJettyAuthenticator`的securityHandler并注入适当的安全约束来保护使用[camel-jetty](http://camel.apache.org/jetty.html)组件实现的Apache Camel端点。 您可以使用类似以下配置将`OSGI-INF/blueprint/blueprint.xml`文件添加到Camel应用程序中。 角色，安全约束映射和Keycloak适配器配置可能略有不同，具体取决于您的环境和需求。

例如：

```xml
<?xml version="1.0" encoding="UTF-8"?>

<blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:camel="http://camel.apache.org/schema/blueprint"
           xsi:schemaLocation="
       http://www.osgi.org/xmlns/blueprint/v1.0.0 http://www.osgi.org/xmlns/blueprint/v1.0.0/blueprint.xsd
       http://camel.apache.org/schema/blueprint http://camel.apache.org/schema/blueprint/camel-blueprint.xsd">

    <bean id="kcAdapterConfig" class="org.keycloak.representations.adapters.config.AdapterConfig">
        <property name="realm" value="demo"/>
        <property name="resource" value="admin-camel-endpoint"/>
        <property name="bearerOnly" value="true"/>
        <property name="authServerUrl" value="http://localhost:8080/auth" />
        <property name="sslRequired" value="EXTERNAL"/>
    </bean>

    <bean id="keycloakAuthenticator" class="org.keycloak.adapters.jetty.KeycloakJettyAuthenticator">
        <property name="adapterConfig" ref="kcAdapterConfig"/>
    </bean>

    <bean id="constraint" class="org.eclipse.jetty.util.security.Constraint">
        <property name="name" value="Customers"/>
        <property name="roles">
            <list>
                <value>admin</value>
            </list>
        </property>
        <property name="authenticate" value="true"/>
        <property name="dataConstraint" value="0"/>
    </bean>

    <bean id="constraintMapping" class="org.eclipse.jetty.security.ConstraintMapping">
        <property name="constraint" ref="constraint"/>
        <property name="pathSpec" value="/*"/>
    </bean>

    <bean id="securityHandler" class="org.eclipse.jetty.security.ConstraintSecurityHandler">
        <property name="authenticator" ref="keycloakAuthenticator" />
        <property name="constraintMappings">
            <list>
                <ref component-id="constraintMapping" />
            </list>
        </property>
        <property name="authMethod" value="BASIC"/>
        <property name="realmName" value="does-not-matter"/>
    </bean>

    <bean id="sessionHandler" class="org.keycloak.adapters.jetty.spi.WrappingSessionHandler">
        <property name="handler" ref="securityHandler" />
    </bean>

    <bean id="helloProcessor" class="org.keycloak.example.CamelHelloProcessor" />

    <camelContext id="blueprintContext"
                  trace="false"
                  xmlns="http://camel.apache.org/schema/blueprint">
        <route id="httpBridge">
            <from uri="jetty:http://0.0.0.0:8383/admin-camel-endpoint?handlers=sessionHandler&amp;matchOnUriPrefix=true" />
            <process ref="helloProcessor" />
            <log message="The message from camel endpoint contains ${body}"/>
        </route>
    </camelContext>

</blueprint>
```

- `META-INF/MANIFEST.MF`中的`Import-Package`需要包含这些导入：

```properties
javax.servlet;version="[3,4)",
javax.servlet.http;version="[3,4)",
org.apache.camel.*,
org.apache.camel;version="[2.13,3)",
org.eclipse.jetty.security;version="[9,10)",
org.eclipse.jetty.server.nio;version="[9,10)",
org.eclipse.jetty.util.security;version="[9,10)",
org.keycloak.*;version="6.0.1",
org.osgi.service.blueprint,
org.osgi.service.blueprint.container,
org.osgi.service.event,
```

##### Camel RestDSL {#Camel_RestDSL}

Camel Rest DSL是一种Camel功能，用于以流畅的方式定义REST端点。 但您仍必须使用特定的实现类，并提供有关如何与Keycloak集成的说明。

配置集成机制的方法取决于您为其配置RestDSL定义的路由的Camel组件。

以下示例显示如何使用Jetty组件配置集成，以及对先前Blueprint示例中定义的某些bean的引用。

```xml
<bean id="securityHandlerRest" class="org.eclipse.jetty.security.ConstraintSecurityHandler">
    <property name="authenticator" ref="keycloakAuthenticator" />
    <property name="constraintMappings">
        <list>
            <ref component-id="constraintMapping" />
        </list>
    </property>
    <property name="authMethod" value="BASIC"/>
    <property name="realmName" value="does-not-matter"/>
</bean>

<bean id="sessionHandlerRest" class="org.keycloak.adapters.jetty.spi.WrappingSessionHandler">
    <property name="handler" ref="securityHandlerRest" />
</bean>


<camelContext id="blueprintContext"
              trace="false"
              xmlns="http://camel.apache.org/schema/blueprint">

    <restConfiguration component="jetty" contextPath="/restdsl"
                       port="8484">
        <!--the link with Keycloak security handlers happens here-->
        <endpointProperty key="handlers" value="sessionHandlerRest"></endpointProperty>
        <endpointProperty key="matchOnUriPrefix" value="true"></endpointProperty>
    </restConfiguration>

    <rest path="/hello" >
        <description>Hello rest service</description>
        <get uri="/{id}" outType="java.lang.String">
            <description>Just an helllo</description>
            <to uri="direct:justDirect" />
        </get>

    </rest>

    <route id="justDirect">
        <from uri="direct:justDirect"/>
        <process ref="helloProcessor" />
        <log message="RestDSL correctly invoked ${body}"/>
        <setBody>
            <constant>(__This second sentence is returned from a Camel RestDSL endpoint__)</constant>
        </setBody>
    </route>

</camelContext>
```

##### 在单独的Jetty引擎上保护Apache CXF端点 {#Securing_an_Apache_CXF_Endpoint_on_a_Separate_Jetty_Engine}

要在单独的Jetty引擎上运行Keycloak保护的CXF端点，请完成以下步骤：

1. 将`META-INF/spring/beans.xml`添加到您的应用程序中，并在其中使用Jetty SecurityHandler声明`httpj：ngine-factory`，并注入`KeycloakJettyAuthenticator`。 CFX JAX-WS应用程序的配置可能类似于以下内容：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:jaxws="http://cxf.apache.org/jaxws"
          xmlns:httpj="http://cxf.apache.org/transports/http-jetty/configuration"
          xsi:schemaLocation="
           http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
           http://cxf.apache.org/jaxws http://cxf.apache.org/schemas/jaxws.xsd
           http://www.springframework.org/schema/osgi http://www.springframework.org/schema/osgi/spring-osgi.xsd
           http://cxf.apache.org/transports/http-jetty/configuration http://cxf.apache.org/schemas/configuration/http-jetty.xsd">
   
       <import resource="classpath:META-INF/cxf/cxf.xml" />
   
       <bean id="kcAdapterConfig" class="org.keycloak.representations.adapters.config.AdapterConfig">
           <property name="realm" value="demo"/>
           <property name="resource" value="custom-cxf-endpoint"/>
           <property name="bearerOnly" value="true"/>
           <property name="authServerUrl" value="http://localhost:8080/auth" />
           <property name="sslRequired" value="EXTERNAL"/>
       </bean>
   
       <bean id="keycloakAuthenticator" class="org.keycloak.adapters.jetty.KeycloakJettyAuthenticator">
           <property name="adapterConfig">
               <ref local="kcAdapterConfig" />
           </property>
       </bean>
   
       <bean id="constraint" class="org.eclipse.jetty.util.security.Constraint">
           <property name="name" value="Customers"/>
           <property name="roles">
               <list>
                   <value>user</value>
               </list>
           </property>
           <property name="authenticate" value="true"/>
           <property name="dataConstraint" value="0"/>
       </bean>
   
       <bean id="constraintMapping" class="org.eclipse.jetty.security.ConstraintMapping">
           <property name="constraint" ref="constraint"/>
           <property name="pathSpec" value="/*"/>
       </bean>
   
       <bean id="securityHandler" class="org.eclipse.jetty.security.ConstraintSecurityHandler">
           <property name="authenticator" ref="keycloakAuthenticator" />
           <property name="constraintMappings">
               <list>
                   <ref local="constraintMapping" />
               </list>
           </property>
           <property name="authMethod" value="BASIC"/>
           <property name="realmName" value="does-not-matter"/>
       </bean>
   
       <httpj:engine-factory bus="cxf" id="kc-cxf-endpoint">
           <httpj:engine port="8282">
               <httpj:handlers>
                   <ref local="securityHandler" />
               </httpj:handlers>
               <httpj:sessionSupport>true</httpj:sessionSupport>
           </httpj:engine>
       </httpj:engine-factory>
   
       <jaxws:endpoint
                       implementor="org.keycloak.example.ws.ProductImpl"
                       address="http://localhost:8282/ProductServiceCF" depends-on="kc-cxf-endpoint" />
   
   </beans>
   ```

   对于CXF JAX-RS应用程序，唯一的区别可能在于依赖于engine-factory的端点配置：

   ```xml
   <jaxrs:server serviceClass="org.keycloak.example.rs.CustomerService" address="http://localhost:8282/rest"
       depends-on="kc-cxf-endpoint">
       <jaxrs:providers>
           <bean class="com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider" />
       </jaxrs:providers>
   </jaxrs:server>
   ```

2. `META-INF/MANIFEST.MF`中的`Import-Package`必须包含那些导入：

```properties
META-INF.cxf;version="[2.7,3.2)",
META-INF.cxf.osgi;version="[2.7,3.2)";resolution:=optional,
org.apache.cxf.bus;version="[2.7,3.2)",
org.apache.cxf.bus.spring;version="[2.7,3.2)",
org.apache.cxf.bus.resource;version="[2.7,3.2)",
org.apache.cxf.transport.http;version="[2.7,3.2)",
org.apache.cxf.*;version="[2.7,3.2)",
org.springframework.beans.factory.config,
org.eclipse.jetty.security;version="[9,10)",
org.eclipse.jetty.util.security;version="[9,10)",
org.keycloak.*;version="6.0.1"
```

##### 在默认Jetty引擎上保护Apache CXF端点 {#Securing_an_Apache_CXF_Endpoint_on_the_Default_Jetty_Engine}

某些服务会在启动时自动附带已部署的servlet。 一个这样的服务是在`http://localhost:8181/cxf`上下文中运行的CXF servlet。 保护这些端点可能很复杂。 Keycloak目前使用的一种方法是ServletReregistrationService，它在启动时取消部署内置servlet，使您能够在Keycloak保护的上下文中重新部署它。

应用程序中的配置文件`OSGI-INF/blueprint/blueprint.xml`可能类似于下面的那个。 请注意，它添加了JAX-RS`customerservice`端点，它是特定于端点的应用程序，但更重要的是，它保护整个`/cxf`上下文。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:jaxrs="http://cxf.apache.org/blueprint/jaxrs"
           xsi:schemaLocation="
                http://www.osgi.org/xmlns/blueprint/v1.0.0 http://www.osgi.org/xmlns/blueprint/v1.0.0/blueprint.xsd
                http://cxf.apache.org/blueprint/jaxrs http://cxf.apache.org/schemas/blueprint/jaxrs.xsd">

    <!-- JAXRS Application -->

    <bean id="customerBean" class="org.keycloak.example.rs.CxfCustomerService" />

    <jaxrs:server id="cxfJaxrsServer" address="/customerservice">
        <jaxrs:providers>
            <bean class="com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider" />
        </jaxrs:providers>
        <jaxrs:serviceBeans>
            <ref component-id="customerBean" />
        </jaxrs:serviceBeans>
    </jaxrs:server>


    <!-- Securing of whole /cxf context by unregister default cxf servlet from paxweb and re-register with applied security constraints -->

    <bean id="cxfConstraintMapping" class="org.eclipse.jetty.security.ConstraintMapping">
        <property name="constraint">
            <bean class="org.eclipse.jetty.util.security.Constraint">
                <property name="name" value="cst1"/>
                <property name="roles">
                    <list>
                        <value>user</value>
                    </list>
                </property>
                <property name="authenticate" value="true"/>
                <property name="dataConstraint" value="0"/>
            </bean>
        </property>
        <property name="pathSpec" value="/cxf/*"/>
    </bean>

    <bean id="cxfKeycloakPaxWebIntegration" class="org.keycloak.adapters.osgi.PaxWebIntegrationService"
          init-method="start" destroy-method="stop">
        <property name="bundleContext" ref="blueprintBundleContext" />
        <property name="jettyWebXmlLocation" value="/WEB-INF/jetty-web.xml" />
        <property name="constraintMappings">
            <list>
                <ref component-id="cxfConstraintMapping" />
            </list>
        </property>
    </bean>

    <bean id="defaultCxfReregistration" class="org.keycloak.adapters.osgi.ServletReregistrationService" depends-on="cxfKeycloakPaxWebIntegration"
          init-method="start" destroy-method="stop">
        <property name="bundleContext" ref="blueprintBundleContext" />
        <property name="managedServiceReference">
            <reference interface="org.osgi.service.cm.ManagedService" filter="(service.pid=org.apache.cxf.osgi)" timeout="5000"  />
        </property>
    </bean>

</blueprint>
```

因此，在默认CXF HTTP目标上运行的所有其他CXF服务也是安全的。 类似地，当取消部署应用程序时，整个`/cxf`上下文也变得不安全。 因此，如[在单独的Jetty引擎上安全CXF应用程序](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse_adapter_cxf_separate)中所述，为您的应用程序使用自己的Jetty引擎，然后为您提供 更好地控制每个应用程序的安全性。

- `WEB-INF`目录可能需要在您的项目中（即使您的项目不是Web应用程序）。 您可能还需要以与[经典WAR应用程序](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse_adapter_classic_war)类似的方式编辑`/WEB-INF/jetty-web.xml`和`/WEB-INF/keycloak.json`文件。 请注意，您不需要`web.xml`文件，因为蓝图配置文件中声明了安全性约束。
- `META-INF/MANIFEST.MF`中的`Import-Package`必须包含以下导入：

```properties
META-INF.cxf;version="[2.7,3.2)",
META-INF.cxf.osgi;version="[2.7,3.2)";resolution:=optional,
org.apache.cxf.transport.http;version="[2.7,3.2)",
org.apache.cxf.*;version="[2.7,3.2)",
com.fasterxml.jackson.jaxrs.json;version="[2.5,3)",
org.eclipse.jetty.security;version="[9,10)",
org.eclipse.jetty.util.security;version="[9,10)",
org.keycloak.*;version="6.0.1",
org.keycloak.adapters.jetty;version="6.0.1",
*;resolution:=optional
```

##### 安全Fuse管理服务 {#Securing_Fuse_Administration_Services}

###### 使用SSH身份验证来融合终端 {#Using_SSH_Authentication_to_Fuse_Terminal}

Keycloak主要处理用于Web应用程序身份验证的用例; 但是，如果您的其他Web服务和应用程序受Keycloak保护，则使用Keycloak凭据保护非Web管理服务（如SSH）是最佳实践。 您可以使用JAAS登录模块执行此操作，该模块允许远程连接到Keycloak并根据[资源所有者密码凭据](https://www.keycloak.org/docs/latest/securing_apps/index.html#_resource_owner_password_credentials_flow)验证凭据。

要启用SSH身份验证，请完成以下步骤：

1. 在Keycloak中创建一个客户端（例如，`ssh-jmx-admin-client`），它将用于SSH身份验证。 此客户端需要将`Direct Access Grants Enabled`选为`On`。

2. 在`$FUSE_HOME/etc/org.apache.karaf.shell.cfg`文件中，更新或指定此属性：

   ```properties
   sshRealm=keycloak
   ```

3. 添加`$FUSE_HOME/etc/keycloak-direct-access.json`文件，其内容类似于以下内容（基于您的环境和Keycloak客户端设置）：

   ```json
   {
       "realm": "demo",
       "resource": "ssh-jmx-admin-client",
       "ssl-required" : "external",
       "auth-server-url" : "http://localhost:8080/auth",
       "credentials": {
           "secret": "password"
       }
   }
   ```

   此文件指定客户端应用程序配置，该命令由来自`keycloak` JAAS领域的JAAS DirectAccessGrantsLoginModule用于SSH身份验证。

4. 启动Fuse并安装`keycloak` JAAS领域。 最简单的方法是安装`keycloak-jaas`功能，它具有预定义的JAAS领域。 您可以使用自己的`keycloak` JAAS领域覆盖该功能的预定义领域。 有关详细信息，请参阅[JBoss Fuse文档](https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.3/html-single/security_guide/#ESBSecureContainer)。

   在Fuse终端中使用以下命令：

   ```
   features:addurl mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
   features:install keycloak-jaas
   ```

5. 通过在终端中键入以下内容，使用SSH作为`admin`用户登录：

   ```bash
   ssh -o PubkeyAuthentication=no -p 8101 admin@localhost
   ```

6. 使用密码`password`登录。

> 在某些更高版本的操作系统上，您可能还需要使用SSH命令的-o选项`-o HostKeyAlgorithms=+ssh-dss`，因为以后的SSH客户端默认情况下不允许使用`ssh-dss`算法。 但是，默认情况下，它目前用于JBoss Fuse 6.3.0 Rollup 5。

请注意，用户需要具有领域角色`admin`来执行所有操作或其他角色来执行操作子集（例如，**viewer**角色限制用户仅运行只读的Karaf命令）。 可用角色在`$FUSE_HOME/etc/org.apache.karaf.shell.cfg`或`$FUSE_HOME/etc/system.properties`中配置。

###### 使用JMX身份验证 {#Using_JMX_Authentication}

如果要使用jconsole或其他外部工具通过RMI远程连接到JMX，则可能需要JMX身份验证。 否则最好使用hawt.io/jolokia，因为jolokia代理默认安装在hawt.io中。 有关详细信息，请参阅[Hawtio管理控制台](https://www.keycloak.org/docs/latest/securing_apps/index.html#_hawtio)。

要使用JMX身份验证，请完成以下步骤：

1. 在`$FUSE_HOME/etc/org.apache.karaf.management.cfg`文件中，将jmxRealm属性更改为：

   ```properties
   jmxRealm=keycloak
   ```

2. 安装`keycloak-jaas`功能并配置`$FUSE_HOME/etc/keycloak-direct-access.json`文件，如上面的SSH部分所述。

3. 在jconsole中，您可以使用以下URL：

```
service:jmx:rmi://localhost:44444/jndi/rmi://localhost:1099/karaf-root
```

和凭据：admin/password（根据您的环境，具有管理员权限的用户）。

##### 保护Hawtio管理控制台 {#Securing_the_Hawtio_Administration_Console}

要使用Keycloak保护Hawtio管理控制台，请完成以下步骤:

1. 将这些属性添加到`$FUSE_HOME/etc/system.properties`文件中：

   ```properties
   hawtio.keycloakEnabled=true
   hawtio.realm=keycloak
   hawtio.keycloakClientConfig=file://${karaf.base}/etc/keycloak-hawtio-client.json
   hawtio.rolePrincipalClasses=org.keycloak.adapters.jaas.RolePrincipal,org.apache.karaf.jaas.boot.principal.RolePrincipal
   ```

2. 在您的领域的Keycloak管理控制台中创建一个客户端。 例如，在Keycloak`demo`领域，创建一个客户端`hawtio-client`，指定`public`作为Access Type，并指定一个指向Hawtio的重定向URI：`http://localhost:8181/hawtio/*`。 您还必须配置相应的Web Origin（在本例中为`http://localhost:8181`）。

3. 使用类似于下面示例中所示的内容在`$FUSE_HOME/etc`目录中创建`keycloak-hawtio-client.json`文件。 根据您的Keycloak环境更改`realm`，`resource`和`auth-server-url`属性。 `resource`属性必须指向上一步中创建的客户端。 该文件由客户端（Hawtio JavaScript应用程序）使用。

   ```json
   {
     "realm" : "demo",
     "resource" : "hawtio-client",
     "auth-server-url" : "http://localhost:8080/auth",
     "ssl-required" : "external",
     "public-client" : true
   }
   ```

4. 使用类似于下面示例中所示的内容在`$FUSE_HOME/etc` 目录中创建`keycloak-hawtio.json`文件。 根据您的Keycloak环境更改`realm`和`auth-server-url`属性。 此文件由服务器（JAAS登录模块）端的适配器使用。

   ```json
   {
     "realm" : "demo",
     "resource" : "jaas",
     "bearer-only" : true,
     "auth-server-url" : "http://localhost:8080/auth",
     "ssl-required" : "external",
     "use-resource-role-mappings": false,
     "principal-attribute": "preferred_username"
   }
   ```

5. 启动JBoss Fuse 6.3.0 Rollup 5并安装keycloak功能（如果尚未安装）。 Karaf终端中的命令与此示例类似：

   ```
   features:addurl mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
   features:install keycloak
   ```

6. 转到<http://localhost:8181/hawtio>并以Keycloak领域的用户身份登录。

   请注意，用户需要具有适当的领域角色才能成功向Hawtio进行身份验证。 可用角色在`hawtio.roles`中的`$FUSE_HOME/etc/system.properties`文件中配置。

###### Securing Hawtio on JBoss EAP 6.4 {#}

To run Hawtio on the JBoss EAP 6.4 server, complete the following steps:

1. Set up Keycloak as described in the previous section, Securing the Hawtio Administration Console. It is assumed that:

   - you have a Keycloak realm `demo` and client `hawtio-client`
   - your Keycloak is running on `localhost:8080`
   - the JBoss EAP 6.4 server with deployed Hawtio will be running on `localhost:8181`. The directory with this server is referred in next steps as `$EAP_HOME`.

2. Copy the `hawtio-wildfly-1.4.0.redhat-630254.war` archive to the `$EAP_HOME/standalone/configuration`directory. For more details about deploying Hawtio see the [Fuse Hawtio documentation](https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.3/html-single/deploying_into_a_web_server/).

3. Copy the `keycloak-hawtio.json` and `keycloak-hawtio-client.json` files with the above content to the `$EAP_HOME/standalone/configuration` directory.

4. Install the Keycloak adapter subsystem to your JBoss EAP 6.4 server as described in the [JBoss adapter documentation](https://www.keycloak.org/docs/latest/securing_apps/index.html#_jboss_adapter).

5. In the `$EAP_HOME/standalone/configuration/standalone.xml` file configure the system properties as in this example:

   ```
   <extensions>
   ...
   </extensions>
   
   <system-properties>
       <property name="hawtio.authenticationEnabled" value="true" />
       <property name="hawtio.realm" value="hawtio" />
       <property name="hawtio.roles" value="admin,viewer" />
       <property name="hawtio.rolePrincipalClasses" value="org.keycloak.adapters.jaas.RolePrincipal" />
       <property name="hawtio.keycloakEnabled" value="true" />
       <property name="hawtio.keycloakClientConfig" value="${jboss.server.config.dir}/keycloak-hawtio-client.json" />
       <property name="hawtio.keycloakServerConfig" value="${jboss.server.config.dir}/keycloak-hawtio.json" />
   </system-properties>
   ```

6. Add the Hawtio realm to the same file in the `security-domains` section:

   ```
   <security-domain name="hawtio" cache-type="default">
       <authentication>
           <login-module code="org.keycloak.adapters.jaas.BearerTokenLoginModule" flag="required">
               <module-option name="keycloak-config-file" value="${hawtio.keycloakServerConfig}"/>
           </login-module>
       </authentication>
   </security-domain>
   ```

7. Add the `secure-deployment` section `hawtio` to the adapter subsystem. This ensures that the Hawtio WAR is able to find the JAAS login module classes.

   ```
   <subsystem xmlns="urn:jboss:domain:keycloak:1.1">
       <secure-deployment name="hawtio-wildfly-1.4.0.redhat-630254.war" />
   </subsystem>
   ```

8. Restart the JBoss EAP 6.4 server with Hawtio:

   ```
   cd $EAP_HOME/bin
   ./standalone.sh -Djboss.socket.binding.port-offset=101
   ```

9. Access Hawtio at <http://localhost:8181/hawtio>. It is secured by Keycloak.

#### 2.1.5. JBoss Fuse 7 Adapter {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/fuse7-adapter.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/fuse7-adapter.adoc)

Keycloak supports securing your web applications running inside [JBoss Fuse 7](https://developers.redhat.com/products/fuse/overview/).

JBoss Fuse 7 leverages Undertow adapter which is essentially the same as [EAP 7 / WildFly Adapter](https://www.keycloak.org/docs/latest/securing_apps/index.html#_jboss_adapter) as JBoss Fuse 7.2.0 is bundled with [Undertow HTTP engine](http://undertow.io/) under the covers and Undertow is used for running various kinds of web applications.

|      | The only supported version of Fuse 7 is the latest release. If you use earlier versions of Fuse 7, it is possible that some functions will not work correctly. In particular, integration will not work at all for versions of Fuse 7 lower than 7.0.1. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Security for the following items is supported for Fuse:

- Classic WAR applications deployed on Fuse with Pax Web War Extender
- Servlets deployed on Fuse as OSGI services with Pax Web Whiteboard Extender and additionally servlets registered through org.osgi.service.http.HttpService#registerServlet() which is standard OSGi Enterprise HTTP Service
- [Apache Camel](http://camel.apache.org/) Undertow endpoints running with the [Camel Undertow](http://camel.apache.org/undertow.html) component
- [Apache CXF](http://cxf.apache.org/) endpoints running on their own separate Undertow engine
- [Apache CXF](http://cxf.apache.org/) endpoints running on the default engine provided by the CXF servlet
- SSH and JMX admin access
- [Hawtio administration console](https://hawt.io/)

##### Securing Your Web Applications Inside Fuse 7 {#}

You must first install the Keycloak Karaf feature. Next you will need to perform the steps according to the type of application you want to secure. All referenced web applications require injecting the Keycloak Undertow authentication mechanism into the underlying web server. The steps to achieve this depend on the application type. The details are described below.

The best place to start is look at Fuse demo bundled as part of Keycloak examples in directory `fuse` . Most of the steps should be understandable from testing and understanding the demo.

##### Installing the Keycloak Feature {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/fuse7/install-feature.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/fuse7/install-feature.adoc)

You must first install the `keycloak-pax-http-undertow` and `keycloak-jaas` features in the JBoss Fuse environment. The `keycloak-pax-http-undertow` feature includes the Fuse adapter and all third-party dependencies. The `keycloak-jaas` contains JAAS module used in realm for SSH and JMX authentication. You can install it either from the Maven repository or from an archive.

###### Installing from the Maven Repository {#}

As a prerequisite, you must be online and have access to the Maven repository.

For community it’s sufficient to be online as all the artifacts and 3rd party dependencies should be available in the maven central repository.

To install the keycloak feature using the Maven repository, complete the following steps:

1. Start JBoss Fuse 7.2.0; then in the Karaf terminal type:

   ```
   feature:repo-add mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
   feature:install keycloak-pax-http-undertow keycloak-jaas
   ```

2. You might also need to install the Undertow feature:

   ```
   feature:install pax-http-undertow
   ```

3. Ensure that the features were installed:

```
feature:list | grep keycloak
```

###### Installing from the ZIP bundle {#}

This is useful if you are offline or do not want to use Maven to obtain the JAR files and other artifacts.

To install the Fuse adapter from the ZIP archive, complete the following steps:

1. Download the Keycloak Fuse adapter ZIP archive.

2. Unzip it into the root directory of JBoss Fuse. The dependencies are then installed under the `system` directory. You can overwrite all existing jar files.

   Use this for JBoss Fuse 7.2.0:

   ```
   cd /path-to-fuse/fuse-karaf-7.z
   unzip -q /path-to-adapter-zip/keycloak-fuse-adapter-6.0.1.zip
   ```

3. Start Fuse and run these commands in the fuse/karaf terminal:

   ```
   feature:repo-add mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
   feature:install keycloak-pax-http-undertow keycloak-jaas
   ```

4. Install the corresponding Undertow adapter. Since the artifacts are available directly in the JBoss Fuse `system` directory, you do not need to use the Maven repository.

##### Securing a Classic WAR Application {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/fuse7/classic-war.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/fuse7/classic-war.adoc)

The needed steps to secure your WAR application are:

1. In the `/WEB-INF/web.xml` file, declare the necessary:

   - security constraints in the <security-constraint> element

   - login configuration in the <login-config> element. Make sure that the `<auth-method>` is `KEYCLOAK`.

   - security roles in the <security-role> element

     For example:

     ```
     <?xml version="1.0" encoding="UTF-8"?>
     <web-app xmlns="http://java.sun.com/xml/ns/javaee"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
              version="3.0">
     
         <module-name>customer-portal</module-name>
     
         <welcome-file-list>
             <welcome-file>index.html</welcome-file>
         </welcome-file-list>
     
         <security-constraint>
             <web-resource-collection>
                 <web-resource-name>Customers</web-resource-name>
                 <url-pattern>/customers/*</url-pattern>
             </web-resource-collection>
             <auth-constraint>
                 <role-name>user</role-name>
             </auth-constraint>
         </security-constraint>
     
         <login-config>
             <auth-method>KEYCLOAK</auth-method>
             <realm-name>does-not-matter</realm-name>
         </login-config>
     
         <security-role>
             <role-name>admin</role-name>
         </security-role>
         <security-role>
             <role-name>user</role-name>
         </security-role>
     </web-app>
     ```

2. Within the `/WEB-INF/` directory of your WAR, create a new file, keycloak.json. The format of this configuration file is described in the [Java Adapters Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_java_adapter_config) section. It is also possible to make this file available externally as described in [Configuring the External Adapter](https://www.keycloak.org/docs/latest/securing_apps/index.html#config_external_adapter).

   For example:

   ```
   {
       "realm": "demo",
       "resource": "customer-portal",
       "auth-server-url": "http://localhost:8080/auth",
       "ssl-required" : "external",
       "credentials": {
           "secret": "password"
       }
   }
   ```

3. Contrary to the Fuse 6 adapter, there are no special OSGi imports needed in MANIFEST.MF.

###### Configuration Resolvers {#}

The `keycloak.json` adapter configuration file can be stored inside a bundle, which is default behaviour, or in a directory on a filesystem. To specify the actual source of the configuration file, set the `keycloak.config.resolver` deployment parameter to the desired configuration resolver class. For example, in a classic WAR application, set the `keycloak.config.resolver` context parameter in `web.xml` file like this:

```
<context-param>
    <param-name>keycloak.config.resolver</param-name>
    <param-value>org.keycloak.adapters.osgi.PathBasedKeycloakConfigResolver</param-value>
</context-param>
```

The following resolvers are available for `keycloak.config.resolver`:

- org.keycloak.adapters.osgi.BundleBasedKeycloakConfigResolver

  This is the default resolver. The configuration file is expected inside the OSGi bundle that is being secured. By default, it loads file named `WEB-INF/keycloak.json` but this file name can be configured via `configLocation` property.

- org.keycloak.adapters.osgi.PathBasedKeycloakConfigResolver

  This resolver searches for a file called `<your_web_context>-keycloak.json` inside a folder that is specified by `keycloak.config` system property. If `keycloak.config` is not set, `karaf.etc` system property is used instead.For example, if your web application is deployed into context `my-portal`, then your adapter configuration would be loaded either from the `${keycloak.config}/my-portal-keycloak.json` file, or from `${karaf.etc}/my-portal-keycloak.json`.

- org.keycloak.adapters.osgi.HierarchicalPathBasedKeycloakConfigResolver

  This resolver is similar to `PathBasedKeycloakConfigResolver` above, where for given URI path, configuration locations are checked from most to least specific.For example, for `/my/web-app/context` URI, the following configuration locations are searched for existence until the first one exists:`${karaf.etc}/my-web-app-context-keycloak.json``${karaf.etc}/my-web-app-keycloak.json``${karaf.etc}/my-keycloak.json``${karaf.etc}/keycloak.json`

##### Securing a Servlet Deployed as an OSGI Service {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/fuse7/servlet-whiteboard.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/fuse7/servlet-whiteboard.adoc)

You can use this method if you have a servlet class inside your OSGI bundled project that is not deployed as a classic WAR application. Fuse uses Pax Web Whiteboard Extender to deploy such servlets as web applications.

To secure your servlet with Keycloak, complete the following steps:

1. Keycloak provides `org.keycloak.adapters.osgi.undertow.PaxWebIntegrationService`, which allows configuring authentication method and security constraints for your application. You need to declare such services in the `OSGI-INF/blueprint/blueprint.xml` file inside your application. Note that your servlet needs to depend on it. An example configuration:

   ```
   <?xml version="1.0" encoding="UTF-8"?>
   <blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://www.osgi.org/xmlns/blueprint/v1.0.0 http://www.osgi.org/xmlns/blueprint/v1.0.0/blueprint.xsd">
   
       <bean id="servletConstraintMapping" class="org.keycloak.adapters.osgi.PaxWebSecurityConstraintMapping">
           <property name="roles">
               <list>
                   <value>user</value>
               </list>
           </property>
           <property name="authentication" value="true"/>
           <property name="url" value="/product-portal/*"/>
       </bean>
   
       <!-- This handles the integration and setting the login-config and security-constraints parameters -->
       <bean id="keycloakPaxWebIntegration" class="org.keycloak.adapters.osgi.undertow.PaxWebIntegrationService"
             init-method="start" destroy-method="stop">
           <property name="bundleContext" ref="blueprintBundleContext" />
           <property name="constraintMappings">
               <list>
                   <ref component-id="servletConstraintMapping" />
               </list>
           </property>
       </bean>
   
       <bean id="productServlet" class="org.keycloak.example.ProductPortalServlet" depends-on="keycloakPaxWebIntegration" />
   
       <service ref="productServlet" interface="javax.servlet.Servlet">
           <service-properties>
               <entry key="alias" value="/product-portal" />
               <entry key="servlet-name" value="ProductServlet" />
               <entry key="keycloak.config.file" value="/keycloak.json" />
           </service-properties>
       </service>
   </blueprint>
   ```

   - You might need to have the `WEB-INF` directory inside your project (even if your project is not a web application) and create the `/WEB-INF/keycloak.json` file as described in the [Classic WAR application](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_adapter_classic_war) section. Note you don’t need the `web.xml` file as the security-constraints are declared in the blueprint configuration file.

2. Contrary to the Fuse 6 adapter, there are no special OSGi imports needed in MANIFEST.MF.

##### Securing an Apache Camel Application {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/fuse7/camel.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/fuse7/camel.adoc)

You can secure Apache Camel endpoints implemented with the [camel-undertow](http://camel.apache.org/undertow.html) component by injecting the proper security constraints via blueprint and updating the used component to `undertow-keycloak`. You have to add the `OSGI-INF/blueprint/blueprint.xml` file to your Camel application with a similar configuration as below. The roles and security constraint mappings, and adapter configuration might differ slightly depending on your environment and needs.

Compared to the standard `undertow` component, `undertow-keycloak` component adds two new properties:

- `configResolver` is a resolver bean that supplies Keycloak adapter configuration. Available resolvers are listed in [Configuration Resolvers](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_config_external_adapter) section.
- `allowedRoles` is a comma-separated list of roles. User accessing the service has to have at least one role to be permitted the access.

For example:

```
<?xml version="1.0" encoding="UTF-8"?>
<blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:camel="http://camel.apache.org/schema/blueprint"
           xsi:schemaLocation="
       http://www.osgi.org/xmlns/blueprint/v1.0.0 http://www.osgi.org/xmlns/blueprint/v1.0.0/blueprint.xsd
       http://camel.apache.org/schema/blueprint http://camel.apache.org/schema/blueprint/camel-blueprint-2.17.1.xsd">

    <bean id="keycloakConfigResolver" class="org.keycloak.adapters.osgi.BundleBasedKeycloakConfigResolver" >
        <property name="bundleContext" ref="blueprintBundleContext" />
    </bean>

    <bean id="helloProcessor" class="org.keycloak.example.CamelHelloProcessor" />

    <camelContext id="blueprintContext"
                  trace="false"
                  xmlns="http://camel.apache.org/schema/blueprint">

        <route id="httpBridge">
            <from uri="undertow-keycloak:http://0.0.0.0:8383/admin-camel-endpoint?matchOnUriPrefix=true&amp;configResolver=#keycloakConfigResolver&amp;allowedRoles=admin" />
            <process ref="helloProcessor" />
            <log message="The message from camel endpoint contains ${body}"/>
        </route>

    </camelContext>

</blueprint>
```

- The `Import-Package` in `META-INF/MANIFEST.MF` needs to contain these imports:

```
javax.servlet;version="[3,4)",
javax.servlet.http;version="[3,4)",
javax.net.ssl,
org.apache.camel.*,
org.apache.camel;version="[2.13,3)",
io.undertow.*,
org.keycloak.*;version="6.0.1",
org.osgi.service.blueprint,
org.osgi.service.blueprint.container
```

##### Camel RestDSL {#}

Camel RestDSL is a Camel feature used to define your REST endpoints in a fluent way. But you must still use specific implementation classes and provide instructions on how to integrate with Keycloak.

The way to configure the integration mechanism depends on the Camel component for which you configure your RestDSL-defined routes.

The following example shows how to configure integration using the `undertow-keycloak` component, with references to some of the beans defined in previous Blueprint example.

```
<camelContext id="blueprintContext"
              trace="false"
              xmlns="http://camel.apache.org/schema/blueprint">

    <!--the link with Keycloak security handlers happens by using undertow-keycloak component -->
    <restConfiguration apiComponent="undertow-keycloak" contextPath="/restdsl" port="8484">
        <endpointProperty key="configResolver" value="#keycloakConfigResolver" />
        <endpointProperty key="allowedRoles" value="admin,superadmin" />
    </restConfiguration>

    <rest path="/hello" >
        <description>Hello rest service</description>
        <get uri="/{id}" outType="java.lang.String">
            <description>Just a hello</description>
            <to uri="direct:justDirect" />
        </get>

    </rest>

    <route id="justDirect">
        <from uri="direct:justDirect"/>
        <process ref="helloProcessor" />
        <log message="RestDSL correctly invoked ${body}"/>
        <setBody>
            <constant>(__This second sentence is returned from a Camel RestDSL endpoint__)</constant>
        </setBody>
    </route>

</camelContext>
```

##### Securing an Apache CXF Endpoint on a Separate Undertow Engine {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/fuse7/cxf-separate.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/fuse7/cxf-separate.adoc)

To run your CXF endpoints secured by Keycloak on a separate Undertow engine, complete the following steps:

1. Add `OSGI-INF/blueprint/blueprint.xml` to your application, and in it, add the proper configuration resolver bean similarly to [Camel configuration](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_adapter_camel). In the `httpu:engine-factory` declare `org.keycloak.adapters.osgi.undertow.CxfKeycloakAuthHandler` handler using that camel configuration. The configuration for a CFX JAX-WS application might resemble this one:

   ```
   <?xml version="1.0" encoding="UTF-8"?>
   <blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xmlns:jaxws="http://cxf.apache.org/blueprint/jaxws"
              xmlns:cxf="http://cxf.apache.org/blueprint/core"
              xmlns:httpu="http://cxf.apache.org/transports/http-undertow/configuration".
              xsi:schemaLocation="
         http://cxf.apache.org/transports/http-undertow/configuration http://cxf.apache.org/schemas/configuration/http-undertow.xsd
         http://cxf.apache.org/blueprint/core http://cxf.apache.org/schemas/blueprint/core.xsd
         http://cxf.apache.org/blueprint/jaxws http://cxf.apache.org/schemas/blueprint/jaxws.xsd">
   
       <bean id="keycloakConfigResolver" class="org.keycloak.adapters.osgi.BundleBasedKeycloakConfigResolver" >
           <property name="bundleContext" ref="blueprintBundleContext" />
       </bean>
   
       <httpu:engine-factory bus="cxf" id="kc-cxf-endpoint">
           <httpu:engine port="8282">
               <httpu:handlers>
                   <bean class="org.keycloak.adapters.osgi.undertow.CxfKeycloakAuthHandler">
                       <property name="configResolver" ref="keycloakConfigResolver" />
                   </bean>
               </httpu:handlers>
           </httpu:engine>
       </httpu:engine-factory>
   
       <jaxws:endpoint implementor="org.keycloak.example.ws.ProductImpl"
                       address="http://localhost:8282/ProductServiceCF" depends-on="kc-cxf-endpoint"/>
   
   </blueprint>
   ```

   For the CXF JAX-RS application, the only difference might be in the configuration of the endpoint dependent on engine-factory:

   ```
   <jaxrs:server serviceClass="org.keycloak.example.rs.CustomerService" address="http://localhost:8282/rest"
       depends-on="kc-cxf-endpoint">
       <jaxrs:providers>
           <bean class="com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider" />
       </jaxrs:providers>
   </jaxrs:server>
   ```

2. The `Import-Package` in `META-INF/MANIFEST.MF` must contain those imports:

```
META-INF.cxf;version="[2.7,3.3)",
META-INF.cxf.osgi;version="[2.7,3.3)";resolution:=optional,
org.apache.cxf.bus;version="[2.7,3.3)",
org.apache.cxf.bus.spring;version="[2.7,3.3)",
org.apache.cxf.bus.resource;version="[2.7,3.3)",
org.apache.cxf.transport.http;version="[2.7,3.3)",
org.apache.cxf.*;version="[2.7,3.3)",
org.springframework.beans.factory.config,
org.keycloak.*;version="6.0.1"
```

##### Securing an Apache CXF Endpoint on the Default Undertow Engine {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/fuse7/cxf-builtin.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/fuse7/cxf-builtin.adoc)

Some services automatically come with deployed servlets on startup. One such service is the CXF servlet running in the http://localhost:8181/cxf context. Fuse’s Pax Web supports altering existing contexts via configuration admin. This can be used to secure endpoints by Keycloak.

The configuration file `OSGI-INF/blueprint/blueprint.xml` inside your application might resemble the one below. Note that it adds the JAX-RS `customerservice` endpoint, which is endpoint-specific to your application.

```
<?xml version="1.0" encoding="UTF-8"?>
<blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:jaxrs="http://cxf.apache.org/blueprint/jaxrs"
           xsi:schemaLocation="
                http://www.osgi.org/xmlns/blueprint/v1.0.0 http://www.osgi.org/xmlns/blueprint/v1.0.0/blueprint.xsd
                http://cxf.apache.org/blueprint/jaxrs http://cxf.apache.org/schemas/blueprint/jaxrs.xsd">

    <!-- JAXRS Application -->
    <bean id="customerBean" class="org.keycloak.example.rs.CxfCustomerService" />

    <jaxrs:server id="cxfJaxrsServer" address="/customerservice">
        <jaxrs:providers>
            <bean class="com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider" />
        </jaxrs:providers>
        <jaxrs:serviceBeans>
            <ref component-id="customerBean" />
        </jaxrs:serviceBeans>
    </jaxrs:server>
</blueprint>
```

Furthermore, you have to create `${karaf.etc}/org.ops4j.pax.web.context-*anyName*.cfg file`. It will be treated as factory PID configuration that is tracked by `pax-web-runtime` bundle. Such configuration may contain the following properties that correspond to some of the properties of standard `web.xml`:

```
bundle.symbolicName = org.apache.cxf.cxf-rt-transports-http
context.id = default

context.param.keycloak.config.resolver = org.keycloak.adapters.osgi.HierarchicalPathBasedKeycloakConfigResolver

login.config.authMethod = KEYCLOAK

security.cxf.url = /cxf/customerservice/*
security.cxf.roles = admin, user
```

For full description of available properties in configuration admin file, please refer to Fuse documentation. The properties above have the following meaning:

- `bundle.symbolicName` and `context.id`

  Identification of the bundle and its deployment context within `org.ops4j.pax.web.service.WebContainer`.

- `context.param.keycloak.config.resolver`

  Provides value of `keycloak.config.resolver` context parameter to the bundle just the same as in `web.xml` for classic WARs. Available resolvers are described in [Configuration Resolvers](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_config_external_adapter) section.

- `login.config.authMethod`

  Authentication method. Must be `KEYCLOAK`.

- `security.*anyName*.url` and `security.*anyName*.roles`

  Values of properties of individual security constraints just as they would be set in `security-constraint/web-resource-collection/url-pattern` and `security-constraint/auth-constraint/role-name` in `web.xml`, respectively. Roles are separated by comma and whitespace around it. The `*anyName*` identifier can be arbitrary but must match for individual properties of the same security constraint.Some Fuse versions contain a bug that requires roles to be separated by `", "` (comma and single space). Make sure you use precisely this notation for separating the roles.

The `Import-Package` in `META-INF/MANIFEST.MF` must contain at least these imports:

```
javax.ws.rs;version="[2,3)",
META-INF.cxf;version="[2.7,3.3)",
META-INF.cxf.osgi;version="[2.7,3.3)";resolution:=optional,
org.apache.cxf.transport.http;version="[2.7,3.3)",
org.apache.cxf.*;version="[2.7,3.3)",
com.fasterxml.jackson.jaxrs.json;version="${jackson.version}"
```

##### Securing Fuse Administration Services {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/fuse7/fuse-admin.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/fuse7/fuse-admin.adoc)

###### Using SSH Authentication to Fuse Terminal {#}

Keycloak mainly addresses use cases for authentication of web applications; however, if your other web services and applications are protected with Keycloak, protecting non-web administration services such as SSH with Keycloak credentials is a best pracrice. You can do this using the JAAS login module, which allows remote connection to Keycloak and verifies credentials based on [Resource Owner Password Credentials](https://www.keycloak.org/docs/latest/securing_apps/index.html#_resource_owner_password_credentials_flow).

To enable SSH authentication, complete the following steps:

1. In Keycloak create a client (for example, `ssh-jmx-admin-client`), which will be used for SSH authentication. This client needs to have `Direct Access Grants Enabled` selected to `On`.

2. In the `$FUSE_HOME/etc/org.apache.karaf.shell.cfg` file, update or specify this property:

   ```
   sshRealm=keycloak
   ```

3. Add the `$FUSE_HOME/etc/keycloak-direct-access.json` file with content similar to the following (based on your environment and Keycloak client settings):

   ```
   {
       "realm": "demo",
       "resource": "ssh-jmx-admin-client",
       "ssl-required" : "external",
       "auth-server-url" : "http://localhost:8080/auth",
       "credentials": {
           "secret": "password"
       }
   }
   ```

   This file specifies the client application configuration, which is used by JAAS DirectAccessGrantsLoginModule from the `keycloak` JAAS realm for SSH authentication.

4. Start Fuse and install the `keycloak` JAAS realm. The easiest way is to install the `keycloak-jaas` feature, which has the JAAS realm predefined. You can override the feature’s predefined realm by using your own `keycloak` JAAS realm with higher ranking. For details see the [JBoss Fuse documentation](https://access.redhat.com/documentation/en-us/red_hat_fuse/7.2/html-single/apache_karaf_security_guide/index#ESBSecureContainer).

   Use these commands in the Fuse terminal:

   ```
   features:addurl mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
   features:install keycloak-jaas
   ```

5. Log in using SSH as `admin` user by typing the following in the terminal:

   ```
   ssh -o PubkeyAuthentication=no -p 8101 admin@localhost
   ```

6. Log in with password `password`.

|      | On some later operating systems, you might also need to use the SSH command’s -o option `-o HostKeyAlgorithms=+ssh-dss` because later SSH clients do not allow use of the `ssh-dss` algorithm, by default. However, by default, it is currently used in JBoss Fuse 7.2.0. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Note that the user needs to have realm role `admin` to perform all operations or another role to perform a subset of operations (for example, the **viewer** role that restricts the user to run only read-only Karaf commands). The available roles are configured in `$FUSE_HOME/etc/org.apache.karaf.shell.cfg` or `$FUSE_HOME/etc/system.properties`.

###### Using JMX Authentication {#}

JMX authentication might be necessary if you want to use jconsole or another external tool to remotely connect to JMX through RMI. Otherwise it might be better to use hawt.io/jolokia, since the jolokia agent is installed in hawt.io by default. For more details see [Hawtio Admin Console](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_hawtio).

To use JMX authentication, complete the following steps:

1. In the `$FUSE_HOME/etc/org.apache.karaf.management.cfg` file, change the jmxRealm property to:

   ```
   jmxRealm=keycloak
   ```

2. Install the `keycloak-jaas` feature and configure the `$FUSE_HOME/etc/keycloak-direct-access.json` file as described in the SSH section above.

3. In jconsole you can use a URL such as:

```
service:jmx:rmi://localhost:44444/jndi/rmi://localhost:1099/karaf-root
```

and credentials: admin/password (based on the user with admin privileges according to your environment).

##### Securing the Hawtio Administration Console {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/fuse7/hawtio.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/fuse7/hawtio.adoc)

To secure the Hawtio Administration Console with Keycloak, complete the following steps:

1. Create a client in the Keycloak administration console in your realm. For example, in the Keycloak `demo`realm, create a client `hawtio-client`, specify `public` as the Access Type, and specify a redirect URI pointing to Hawtio: http://localhost:8181/hawtio/*. Configure corresponding Web Origin (in this case, http://localhost:8181). Setup client scope mapping to include *view-profile* client role of *account* client in *Scope* tab in `hawtio-client` client detail.

2. Create the `keycloak-hawtio-client.json` file in the `$FUSE_HOME/etc` directory using content similar to that shown in the example below. Change the `realm`, `resource`, and `auth-server-url` properties according to your Keycloak environment. The `resource` property must point to the client created in the previous step. This file is used by the client (Hawtio JavaScript application) side.

   ```
   {
     "realm" : "demo",
     "clientId" : "hawtio-client",
     "url" : "http://localhost:8080/auth",
     "ssl-required" : "external",
     "public-client" : true
   }
   ```

3. Create the `keycloak-direct-access.json` file in the `$FUSE_HOME/etc` directory using content similar to that shown in the example below. Change the `realm` and `url` properties according to your Keycloak environment. This file is used by JavaScript client.

   ```
   {
     "realm" : "demo",
     "resource" : "ssh-jmx-admin-client",
     "auth-server-url" : "http://localhost:8080/auth",
     "ssl-required" : "external",
     "credentials": {
       "secret": "password"
     }
   }
   ```

4. Create the `keycloak-hawtio.json` file in the `$FUSE_HOME/etc` dicrectory using content similar to that shown in the example below. Change the `realm` and `auth-server-url` properties according to your Keycloak environment. This file is used by the adapters on the server (JAAS Login module) side.

   ```
   {
     "realm" : "demo",
     "resource" : "jaas",
     "bearer-only" : true,
     "auth-server-url" : "http://localhost:8080/auth",
     "ssl-required" : "external",
     "use-resource-role-mappings": false,
     "principal-attribute": "preferred_username"
   }
   ```

5. Start JBoss Fuse 7.2.0, [install the Keycloak feature](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_install_feature). Then type in the Karaf terminal:

   ```
   system:property -p hawtio.keycloakEnabled true
   system:property -p hawtio.realm keycloak
   system:property -p hawtio.keycloakClientConfig file://\${karaf.base}/etc/keycloak-hawtio-client.json
   system:property -p hawtio.rolePrincipalClasses org.keycloak.adapters.jaas.RolePrincipal,org.apache.karaf.jaas.boot.principal.RolePrincipal
   restart io.hawt.hawtio-war
   ```

6. Go to <http://localhost:8181/hawtio> and log in as a user from your Keycloak realm.

   Note that the user needs to have the proper realm role to successfully authenticate to Hawtio. The available roles are configured in the `$FUSE_HOME/etc/system.properties` file in `hawtio.roles`.

#### 2.1.6. Spring Boot Adapter {#}

To be able to secure Spring Boot apps you must add the Keycloak Spring Boot adapter JAR to your app. You then have to provide some extra configuration via normal Spring Boot configuration (`application.properties`). Let’s go over these steps.

##### Adapter Installation {#}

The Keycloak Spring Boot adapter takes advantage of Spring Boot’s autoconfiguration so all you need to do is add the Keycloak Spring Boot starter to your project. They Keycloak Spring Boot Starter is also directly available from the [Spring Start Page](https://start.spring.io/). To add it manually using Maven, add the following to your dependencies:

```
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-spring-boot-starter</artifactId>
</dependency>
```

Add the Adapter BOM dependency:

```
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.keycloak.bom</groupId>
      <artifactId>keycloak-adapter-bom</artifactId>
      <version>6.0.1</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

Currently the following embedded containers are supported and do not require any extra dependencies if using the Starter:

- Tomcat
- Undertow
- Jetty

##### Required Spring Boot Adapter Configuration {#}

This section describes how to configure your Spring Boot app to use Keycloak.

Instead of a `keycloak.json` file, you configure the realm for the Spring Boot Keycloak adapter via the normal Spring Boot configuration. For example:

```
keycloak.realm = demorealm
keycloak.auth-server-url = http://127.0.0.1:8080/auth
keycloak.ssl-required = external
keycloak.resource = demoapp
keycloak.credentials.secret = 11111111-1111-1111-1111-111111111111
keycloak.use-resource-role-mappings = true
```

You can disable the Keycloak Spring Boot Adapter (for example in tests) by setting `keycloak.enabled = false`.

To configure a Policy Enforcer, unlike keycloak.json, `policy-enforcer-config` must be used instead of just `policy-enforcer`.

You also need to specify the Java EE security config that would normally go in the `web.xml`. The Spring Boot Adapter will set the `login-method` to `KEYCLOAK` and configure the `security-constraints` at startup time. Here’s an example configuration:

```
keycloak.securityConstraints[0].authRoles[0] = admin
keycloak.securityConstraints[0].authRoles[1] = user
keycloak.securityConstraints[0].securityCollections[0].name = insecure stuff
keycloak.securityConstraints[0].securityCollections[0].patterns[0] = /insecure

keycloak.securityConstraints[1].authRoles[0] = admin
keycloak.securityConstraints[1].securityCollections[0].name = admin stuff
keycloak.securityConstraints[1].securityCollections[0].patterns[0] = /admin
```

|      | If you plan to deploy your Spring Application as a WAR then you should not use the Spring Boot Adapter and use the dedicated adapter for the application server or servlet container you are using. Your Spring Boot should also contain a `web.xml` file. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

#### 2.1.7. Tomcat 6, 7 and 8 Adapters {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/tomcat-adapter.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/tomcat-adapter.adoc)

To be able to secure WAR apps deployed on Tomcat 6, 7 and 8 you must install the Keycloak Tomcat 6, 7 or 8 adapter into your Tomcat installation. You then have to provide some extra configuration in each WAR you deploy to Tomcat. Let’s go over these steps.

##### Adapter Installation {#}

Adapters are no longer included with the appliance or war distribution. Each adapter is a separate download on the Keycloak download site. They are also available as a maven artifact.

You must unzip the adapter distro into Tomcat’s `lib/` directory. Including adapter’s jars within your WEB-INF/lib directory will not work! The Keycloak adapter is implemented as a Valve and valve code must reside in Tomcat’s main lib/ directory.

```
$ cd $TOMCAT_HOME/lib
$ unzip keycloak-tomcat6-adapter-dist.zip
    or
$ unzip keycloak-tomcat7-adapter-dist.zip
    or
$ unzip keycloak-tomcat8-adapter-dist.zip
```

##### Required Per WAR Configuration {#}

This section describes how to secure a WAR directly by adding config and editing files within your WAR package.

The first thing you must do is create a `META-INF/context.xml` file in your WAR package. This is a Tomcat specific config file and you must define a Keycloak specific Valve.

```
<Context path="/your-context-path">
    <Valve className="org.keycloak.adapters.tomcat.KeycloakAuthenticatorValve"/>
</Context>
```

Next you must create a `keycloak.json` adapter config file within the `WEB-INF` directory of your WAR.

The format of this config file is described in the [Java adapter configuration](https://www.keycloak.org/docs/latest/securing_apps/index.html#_java_adapter_config)

Finally you must specify both a `login-config` and use standard servlet security to specify role-base constraints on your URLs. Here’s an example:

```
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">

        <module-name>customer-portal</module-name>

    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Customers</web-resource-name>
            <url-pattern>/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>user</role-name>
        </auth-constraint>
    </security-constraint>

    <login-config>
        <auth-method>BASIC</auth-method>
        <realm-name>this is ignored currently</realm-name>
    </login-config>

    <security-role>
        <role-name>admin</role-name>
    </security-role>
    <security-role>
        <role-name>user</role-name>
    </security-role>
</web-app>
```

#### 2.1.8. Jetty 9.x Adapters {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/jetty9-adapter.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/jetty9-adapter.adoc)

Keycloak has a separate adapter for Jetty 9.2.x, Jetty 9.3.x and Jetty 9.4.x that you will have to install into your Jetty installation. You then have to provide some extra configuration in each WAR you deploy to Jetty. Let’s go over these steps.

##### Adapter Installation {#}

Adapters are no longer included with the appliance or war distribution. Each adapter is a separate download on the Keycloak download site. They are also available as a maven artifact.

You must unzip the Jetty 9.x distro into Jetty 9.x’s [base directory](https://www.eclipse.org/jetty/documentation/current/startup-base-and-home.html). Including adapter’s jars within your WEB-INF/lib directory will not work! In the example below, the Jetty base is named `your-base`:

```
$ cd your-base
$ unzip keycloak-jetty93-adapter-dist-2.5.0.Final.zip
```

Next, you will have to enable the `keycloak` module for your Jetty base:

```
$ java -jar $JETTY_HOME/start.jar --add-to-startd=keycloak
```

##### Required Per WAR Configuration {#}

This section describes how to secure a WAR directly by adding config and editing files within your WAR package.

The first thing you must do is create a `WEB-INF/jetty-web.xml` file in your WAR package. This is a Jetty specific config file and you must define a Keycloak specific authenticator within it.

```
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Mort Bay Consulting//DTD Configure//EN" "http://www.eclipse.org/jetty/configure_9_0.dtd">
<Configure class="org.eclipse.jetty.webapp.WebAppContext">
    <Get name="securityHandler">
        <Set name="authenticator">
            <New class="org.keycloak.adapters.jetty.KeycloakJettyAuthenticator">
            </New>
        </Set>
    </Get>
</Configure>
```

Next you must create a `keycloak.json` adapter config file within the `WEB-INF` directory of your WAR.

The format of this config file is described in the [Java adapter configuration](https://www.keycloak.org/docs/latest/securing_apps/index.html#_java_adapter_config) section.

|      | The Jetty 9.x adapter will not be able to find the `keycloak.json` file. You will have to define all adapter settings within the `jetty-web.xml` file as described below. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Instead of using keycloak.json, you can define everything within the `jetty-web.xml`. You’ll just have to figure out how the json settings match to the `org.keycloak.representations.adapters.config.AdapterConfig` class.

```
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Mort Bay Consulting//DTD Configure//EN" "http://www.eclipse.org/jetty/configure_9_0.dtd">
<Configure class="org.eclipse.jetty.webapp.WebAppContext">
  <Get name="securityHandler">
    <Set name="authenticator">
        <New class="org.keycloak.adapters.jetty.KeycloakJettyAuthenticator">
            <Set name="adapterConfig">
                <New class="org.keycloak.representations.adapters.config.AdapterConfig">
                    <Set name="realm">tomcat</Set>
                    <Set name="resource">customer-portal</Set>
                    <Set name="authServerUrl">http://localhost:8081/auth</Set>
                    <Set name="sslRequired">external</Set>
                    <Set name="credentials">
                        <Map>
                            <Entry>
                                <Item>secret</Item>
                                <Item>password</Item>
                            </Entry>
                        </Map>
                    </Set>
                </New>
            </Set>
        </New>
    </Set>
  </Get>
</Configure>
```

You do not have to crack open your WAR to secure it with keycloak. Instead create the jetty-web.xml file in your webapps directory with the name of yourwar.xml. Jetty should pick it up. In this mode, you’ll have to declare keycloak.json configuration directly within the xml file.

Finally you must specify both a `login-config` and use standard servlet security to specify role-base constraints on your URLs. Here’s an example:

```
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">

        <module-name>customer-portal</module-name>

    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Customers</web-resource-name>
            <url-pattern>/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>user</role-name>
        </auth-constraint>
        <user-data-constraint>
            <transport-guarantee>CONFIDENTIAL</transport-guarantee>
        </user-data-constraint>
    </security-constraint>

    <login-config>
        <auth-method>BASIC</auth-method>
        <realm-name>this is ignored currently</realm-name>
    </login-config>

    <security-role>
        <role-name>admin</role-name>
    </security-role>
    <security-role>
        <role-name>user</role-name>
    </security-role>
</web-app>
```

#### 2.1.9. Spring Security Adapter {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/spring-security-adapter.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/spring-security-adapter.adoc)

To secure an application with Spring Security and Keycloak, add this adapter as a dependency to your project. You then have to provide some extra beans in your Spring Security configuration file and add the Keycloak security filter to your pipeline.

Unlike the other Keycloak Adapters, you should not configure your security in web.xml. However, keycloak.json is still required.

##### Adapter Installation {#}

Add Keycloak Spring Security adapter as a dependency to your Maven POM or Gradle build.

```
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-spring-security-adapter</artifactId>
    <version>6.0.1</version>
</dependency>
```

##### Spring Security Configuration {#}

The Keycloak Spring Security adapter takes advantage of Spring Security’s flexible security configuration syntax.

###### Java Configuration {#}

Keycloak provides a KeycloakWebSecurityConfigurerAdapter as a convenient base class for creating a [WebSecurityConfigurer](https://docs.spring.io/spring-security/site/docs/4.0.x/apidocs/org/springframework/security/config/annotation/web/WebSecurityConfigurer.html)instance. The implementation allows customization by overriding methods. While its use is not required, it greatly simplifies your security context configuration.

```
@KeycloakConfiguration
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter
{
    /**
     * Registers the KeycloakAuthenticationProvider with the authentication manager.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(keycloakAuthenticationProvider());
    }

    /**
     * Defines the session authentication strategy.
     */
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        super.configure(http);
        http
                .authorizeRequests()
                .antMatchers("/customers*").hasRole("USER")
                .antMatchers("/admin*").hasRole("ADMIN")
                .anyRequest().permitAll();
    }
}
```

You must provide a session authentication strategy bean which should be of type `RegisterSessionAuthenticationStrategy` for public or confidential applications and `NullAuthenticatedSessionStrategy` for bearer-only applications.

Spring Security’s `SessionFixationProtectionStrategy` is currently not supported because it changes the session identifier after login via Keycloak. If the session identifier changes, universal log out will not work because Keycloak is unaware of the new session identifier.

|      | The `@KeycloakConfiguration` annotation is a metadata annotion that defines all annotations that are needed to integrate Keycloak in Spring Security. If you have a complexe Spring Security setup you can simply have a look ath the annotations of the `@KeycloakConfiguration` annotation and create your own custom meta annotation or just use specific Spring annotations for the Keycloak adapter. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

###### XML Configuration {#}

While Spring Security’s XML namespace simplifies configuration, customizing the configuration can be a bit verbose.

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:security="http://www.springframework.org/schema/security"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/security
       http://www.springframework.org/schema/security/spring-security.xsd">

    <context:component-scan base-package="org.keycloak.adapters.springsecurity" />

    <security:authentication-manager alias="authenticationManager">
        <security:authentication-provider ref="keycloakAuthenticationProvider" />
    </security:authentication-manager>

    <bean id="adapterDeploymentContext" class="org.keycloak.adapters.springsecurity.AdapterDeploymentContextFactoryBean">
        <constructor-arg value="/WEB-INF/keycloak.json" />
    </bean>

    <bean id="keycloakAuthenticationEntryPoint" class="org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationEntryPoint" />
    <bean id="keycloakAuthenticationProvider" class="org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider" />
    <bean id="keycloakPreAuthActionsFilter" class="org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter" />
    <bean id="keycloakAuthenticationProcessingFilter" class="org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter">
        <constructor-arg name="authenticationManager" ref="authenticationManager" />
    </bean>

    <bean id="keycloakLogoutHandler" class="org.keycloak.adapters.springsecurity.authentication.KeycloakLogoutHandler">
        <constructor-arg ref="adapterDeploymentContext" />
    </bean>

    <bean id="logoutFilter" class="org.springframework.security.web.authentication.logout.LogoutFilter">
        <constructor-arg name="logoutSuccessUrl" value="/" />
        <constructor-arg name="handlers">
            <list>
                <ref bean="keycloakLogoutHandler" />
                <bean class="org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler" />
            </list>
        </constructor-arg>
        <property name="logoutRequestMatcher">
            <bean class="org.springframework.security.web.util.matcher.AntPathRequestMatcher">
                <constructor-arg name="pattern" value="/sso/logout**" />
                <constructor-arg name="httpMethod" value="GET" />
            </bean>
        </property>
    </bean>

    <security:http auto-config="false" entry-point-ref="keycloakAuthenticationEntryPoint">
        <security:custom-filter ref="keycloakPreAuthActionsFilter" before="LOGOUT_FILTER" />
        <security:custom-filter ref="keycloakAuthenticationProcessingFilter" before="FORM_LOGIN_FILTER" />
        <security:intercept-url pattern="/customers**" access="ROLE_USER" />
        <security:intercept-url pattern="/admin**" access="ROLE_ADMIN" />
        <security:custom-filter ref="logoutFilter" position="LOGOUT_FILTER" />
    </security:http>

</beans>
```

##### Multi Tenancy {#}

The Keycloak Spring Security adapter also supports multi tenancy. Instead of injecting `AdapterDeploymentContextFactoryBean` with the path to `keycloak.json` you can inject an implementation of the `KeycloakConfigResolver` interface. More details on how to implement the `KeycloakConfigResolver` can be found in [Multi Tenancy](https://www.keycloak.org/docs/latest/securing_apps/index.html#_multi_tenancy).

##### Naming Security Roles {#}

Spring Security, when using role-based authentication, requires that role names start with `ROLE_`. For example, an administrator role must be declared in Keycloak as `ROLE_ADMIN` or similar, not simply `ADMIN`.

The class `org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider` supports an optional `org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper` which can be used to map roles coming from Keycloak to roles recognized by Spring Security. Use, for example, `org.springframework.security.core.authority.mapping.SimpleAuthorityMapper` to insert the `ROLE_` prefix and convert the role name to upper case. The class is part of Spring Security Core module.

##### Client to Client Support {#}

To simplify communication between clients, Keycloak provides an extension of Spring’s `RestTemplate` that handles bearer token authentication for you. To enable this feature your security configuration must add the `KeycloakRestTemplate`bean. Note that it must be scoped as a prototype to function correctly.

For Java configuration:

```
@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    ...

    @Autowired
    public KeycloakClientRequestFactory keycloakClientRequestFactory;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public KeycloakRestTemplate keycloakRestTemplate() {
        return new KeycloakRestTemplate(keycloakClientRequestFactory);
    }

    ...
}
```

For XML configuration:

```
<bean id="keycloakRestTemplate" class="org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate" scope="prototype">
    <constructor-arg name="factory" ref="keycloakClientRequestFactory" />
</bean>
```

Your application code can then use `KeycloakRestTemplate` any time it needs to make a call to another client. For example:

```
@Service
public class RemoteProductService implements ProductService {

    @Autowired
    private KeycloakRestTemplate template;

    private String endpoint;

    @Override
    public List<String> getProducts() {
        ResponseEntity<String[]> response = template.getForEntity(endpoint, String[].class);
        return Arrays.asList(response.getBody());
    }
}
```

##### Spring Boot Integration {#}

The Spring Boot and the Spring Security adapters can be combined.

If you are using the Keycloak Spring Boot Starter to make use of the Spring Security adapter you just need to add the Spring Security starter :

```
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

###### Using Spring Boot Configuration {#}

By Default, the Spring Security Adapter looks for a `keycloak.json` configuration file. You can make sure it looks at the configuration provided by the Spring Boot Adapter by adding this bean :

```
@Bean
public KeycloakConfigResolver KeycloakConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
}
```

###### Avoid double bean registration {#}

Spring Boot attempts to eagerly register filter beans with the web application context. Therefore, when running the Keycloak Spring Security adapter in a Spring Boot environment, it may be necessary to add `FilterRegistrationBean`s to your security configuration to prevent the Keycloak filters from being registered twice.

Spring Boot 2.1 also disables `spring.main.allow-bean-definition-overriding` by default. This can mean that an `BeanDefinitionOverrideException` will be encountered if a `Configuration` class extending `KeycloakWebSecurityConfigurerAdapter` registers a bean that is already detected by a `@ComponentScan`. This can be avoided by overriding the registration to use the Boot-specific `@ConditionalOnMissingBean` annotation, as with `HttpSessionManager` below.

```
@Configuration
@EnableWebSecurity
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter
{
    ...

    @Bean
    public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
            KeycloakAuthenticationProcessingFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
            KeycloakPreAuthActionsFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakAuthenticatedActionsFilterBean(
            KeycloakAuthenticatedActionsFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakSecurityContextRequestFilterBean(
        KeycloakSecurityContextRequestFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    @Override
    @ConditionalOnMissingBean(HttpSessionManager.class)
    protected HttpSessionManager httpSessionManager() {
        return new HttpSessionManager();
    }
    ...
}
```

#### 2.1.10. Java Servlet Filter Adapter {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/servlet-filter-adapter.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/servlet-filter-adapter.adoc)

If you are deploying your Java Servlet application on a platform where there is no Keycloak adapter you opt to use the servlet filter adapter. This adapter works a bit differently than the other adapters. You do not define security constraints in web.xml. Instead you define a filter mapping using the Keycloak servlet filter adapter to secure the url patterns you want to secure.

|      | Backchannel logout works a bit differently than the standard adapters. Instead of invalidating the HTTP session it marks the session id as logged out. There’s no standard way to invalidate an HTTP session based on a session id. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

```
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">

        <module-name>application</module-name>

    <filter>
        <filter-name>Keycloak Filter</filter-name>
        <filter-class>org.keycloak.adapters.servlet.KeycloakOIDCFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>Keycloak Filter</filter-name>
        <url-pattern>/keycloak/*</url-pattern>
        <url-pattern>/protected/*</url-pattern>
    </filter-mapping>
</web-app>
```

In the snippet above there are two url-patterns. */protected/** are the files we want protected, while the */keycloak/** url-pattern handles callbacks from the Keycloak server.

If you need to exclude some paths beneath the configured `url-patterns` you can use the Filter init-param `keycloak.config.skipPattern` to configure a regular expression that describes a path-pattern for which the keycloak filter should immediately delegate to the filter-chain. By default no skipPattern is configured.

Patterns are matched against the `requestURI` without the `context-path`. Given the context-path `/myapp` a request for `/myapp/index.html` will be matched with `/index.html` against the skip pattern.

```
<init-param>
    <param-name>keycloak.config.skipPattern</param-name>
    <param-value>^/(path1|path2|path3).*</param-value>
</init-param>
```

Note that you should configure your client in the Keycloak Admin Console with an Admin URL that points to a secured section covered by the filter’s url-pattern.

The Admin URL will make callbacks to the Admin URL to do things like backchannel logout. So, the Admin URL in this example should be `http[s]://hostname/{context-root}/keycloak`.

The Keycloak filter has the same configuration parameters as the other adapters except you must define them as filter init params instead of context params.

To use this filter, include this maven artifact in your WAR poms:

```
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-servlet-filter-adapter</artifactId>
    <version>6.0.1</version>
</dependency>
```

##### Using on OSGi {#}

The servlet filter adapter is packaged as an OSGi bundle, and thus is usable in a generic OSGi environment (R6 and above) with HTTP Service and HTTP Whiteboard.

###### Installation {#}

The adapter and its dependencies are distributed as Maven artifacts, so you’ll need either working Internet connection to access Maven Central, or have the artifacts cached in your local Maven repo.

If you are using Apache Karaf, you can simply install a feature from the Keycloak feature repo:

```
karaf@root()> feature:repo-add mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
karaf@root()> feature:install keycloak-servlet-filter-adapter
```

For other OSGi runtimes, please refer to the runtime documentation on how to install the adapter bundle and its dependencies.

|      | If your OSGi platform is Apache Karaf with Pax Web, you should consider using [JBoss Fuse 6](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse_adapter) or [JBoss Fuse 7](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_adapter)adapters instead. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

###### Configuration {#}

First, the adapter needs to be registered as a servlet filter with the OSGi HTTP Service. The most common ways to do this are programmatic (e.g. via bundle activator) and declarative (using OSGi annotations). We recommend using the latter since it simplifies the process of dynamically registering and un-registering the filter:

```
package mypackage;

import javax.servlet.Filter;
import org.keycloak.adapters.servlet.KeycloakOIDCFilter;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

@Component(
    immediate = true,
    service = Filter.class,
    property = {
        KeycloakOIDCFilter.CONFIG_FILE_PARAM + "=" + "keycloak.json",
        HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN + "=" +"/*",
        HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT + "=" + "(osgi.http.whiteboard.context.name=mycontext)"
    }
)
public class KeycloakFilter extends KeycloakOIDCFilter {
  //
}
```

The above snippet uses OSGi declarative service specification to expose the filter as an OSGI service under `javax.servlet.Filter` class. Once the class is published in the OSGi service registry, it is going to be picked up by OSGi HTTP Service implementation and used for filtering requests for the specified servlet context. This will trigger Keycloak adapter for every request that matches servlet context path + filter path.

Since the component is put under the control of OSGi Configuration Admin Service, it’s properties can be configured dynamically. To do that, either create a `mypackage.KeycloakFilter.cfg` file under the standard config location for your OSGi runtime:

```
keycloak.config.file = /path/to/keycloak.json
osgi.http.whiteboard.filter.pattern = /secure/*
```

or use interactive console, if your runtime allows for that:

```
karaf@root()> config:edit mypackage.KeycloakFilter
karaf@root()> config:property-set keycloak.config.file '${karaf.etc}/keycloak.json'
karaf@root()> config:update
```

If you need more control, like e.g. providing custom `KeycloakConfigResolver` to implement [multi tenancy](https://www.keycloak.org/docs/latest/securing_apps/index.html#_multi_tenancy), you can register the filter programmatically:

```
public class Activator implements BundleActivator {

  private ServiceRegistration registration;

  public void start(BundleContext context) throws Exception {
    Hashtable props = new Hashtable();
    props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN, "/secure/*");
    props.put(KeycloakOIDCFilter.CONFIG_RESOLVER_PARAM, new MyConfigResolver());

    this.registration = context.registerService(Filter.class.getName(), new KeycloakOIDCFilter(), props);
  }

  public void stop(BundleContext context) throws Exception {
    this.registration.unregister();
  }
}
```

Please refer to [Apache Felix HTTP Service](http://felix.apache.org/documentation/subprojects/apache-felix-http-service.html#using-the-osgi-http-whiteboard) for more info on programmatic registration.

#### 2.1.11. JAAS plugin {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/jaas.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/jaas.adoc)

It’s generally not needed to use JAAS for most of the applications, especially if they are HTTP based, and you should most likely choose one of our other adapters. However, some applications and systems may still rely on pure legacy JAAS solution. Keycloak provides two login modules to help in these situations.

The provided login modules are:

- org.keycloak.adapters.jaas.DirectAccessGrantsLoginModule

  This login module allows to authenticate with username/password from Keycloak. It’s using [Resource Owner Password Credentials](https://www.keycloak.org/docs/latest/securing_apps/index.html#_resource_owner_password_credentials_flow) flow to validate if the provided username/password is valid. It’s useful for non-web based systems, which need to rely on JAAS and want to use Keycloak, but can’t use the standard browser based flows due to their non-web nature. Example of such application could be messaging or SSH.

- org.keycloak.adapters.jaas.BearerTokenLoginModule

  This login module allows to authenticate with Keycloak access token passed to it through CallbackHandler as password. It may be useful for example in case, when you have Keycloak access token from standard based authentication flow and your web application then needs to talk to external non-web based system, which rely on JAAS. For example a messaging system.

Both modules use the following configuration properties:

- keycloak-config-file

  The location of the `keycloak.json` configuration file. The configuration file can either be located on the filesystem or on the classpath. If it’s located on the classpath you need to prefix the location with `classpath:` (for example `classpath:/path/keycloak.json`). This is *REQUIRED.*

- `role-principal-class`

  Configure alternative class for Role principals attached to JAAS Subject. Default value is `org.keycloak.adapters.jaas.RolePrincipal`. Note: The class is required to have a constructor with a single `String`argument.

- `scope`

  This option is only applicable to the `DirectAccessGrantsLoginModule`. The specified value will be used as the OAuth2 `scope` parameter in the Resource Owner Password Credentials Grant request.

#### 2.1.12. CLI / Desktop Applications {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/installed-adapter.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/installed-adapter.adoc)

Keycloak supports securing desktop (e.g. Swing, JavaFX) or CLI applications via the `KeycloakInstalled`adapter by performing the authentication step via the system browser.

The `KeycloakInstalled` adapter supports a `desktop` and a `manual` variant. The desktop variant uses the system browser to gather the user credentials. The manual variant reads the user credentials from `STDIN`.

Tip: Google provides some more information about this approach on at [OAuth2InstalledApp](https://developers.google.com/identity/protocols/OAuth2InstalledApp).

##### How it works {#}

To authenticate a user with the `desktop` variant the `KeycloakInstalled` adapter opens a desktop browser window where a user uses the regular Keycloak login pages to login when the `loginDesktop()` method is called on the `KeycloakInstalled` object.

The login page URL is opened with redirect parameter that points to a local `ServerSocket` listening on a free ephemeral port on `localhost` which is started by the adapter.

After a succesful login the `KeycloakInstalled` receives the authorization code from the incoming HTTP request and performs the authorization code flow. Once the code to token exchange is completed the `ServerSocket` is shutdown.

|      | If the user already has an active Keycloak session then the login form is not shown but the code to token exchange is continued, which enables a smooth Web based SSO experience. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

The client eventually receives the tokens (access_token, refresh_token, id_token) which can then be used to call backend services.

The `KeycloakInstalled` adapter provides support for renewal of stale tokens.

##### Adapter Installation {#}

```
<dependency>
        <groupId>org.keycloak</groupId>
        <artifactId>keycloak-installed-adapter</artifactId>
        <version>6.0.1</version>
</dependency>
```

##### Client Configuration {#}

The application needs to be configured as a `public` OpenID Connect client with `Standard Flow Enabled` and http://localhost:* as an allowed `Valid Redirect URI`.

##### Usage {#}

The `KeycloakInstalled` adapter reads it’s configuration from `META-INF/keycloak.json` on the classpath. Custom configurations can be supplied with an `InputStream` or a `KeycloakDeployment` through the `KeycloakInstalled`constructor.

In the example below, the client configuration for `desktop-app` uses the following `keycloak.json`:

```
{
  "realm": "desktop-app-auth",
  "auth-server-url": "http://localhost:8081/auth",
  "ssl-required": "external",
  "resource": "desktop-app",
  "public-client": true,
  "use-resource-role-mappings": true
}
```

the following sketch demonstrates working with the `KeycloakInstalled` adapter:

```
// reads the configuration from classpath: META-INF/keycloak.json
KeycloakInstalled keycloak = new KeycloakInstalled();

// opens desktop browser
keycloak.loginDesktop();

AccessToken token = keycloak.getToken();
// use token to send backend request

// ensure token is valid for at least 30 seconds
long minValidity = 30L;
String tokenString = keycloak.getTokenString(minValidity, TimeUnit.SECONDS);


 // when you want to logout the user.
keycloak.logout();
```

|      | The `KeycloakInstalled` class supports customization of the http responses returned by login / logout requests via the `loginResponseWriter` and `logoutResponseWriter` attributes. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

##### Example {#}

The following provides an example for the configuration mentioned above.

```
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.keycloak.adapters.installed.KeycloakInstalled;
import org.keycloak.representations.AccessToken;

public class DesktopApp {

        public static void main(String[] args) throws Exception {

                KeycloakInstalled keycloak = new KeycloakInstalled();
                keycloak.setLocale(Locale.ENGLISH);
                keycloak.loginDesktop();

                AccessToken token = keycloak.getToken();
                Executors.newSingleThreadExecutor().submit(() -> {

                        System.out.println("Logged in...");
                        System.out.println("Token: " + token.getSubject());
                        System.out.println("Username: " + token.getPreferredUsername());
                        try {
                                System.out.println("AccessToken: " + keycloak.getTokenString());
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }

                        int timeoutSeconds = 20;
                        System.out.printf("Logging out in...%d Seconds%n", timeoutSeconds);
                        try {
                                TimeUnit.SECONDS.sleep(timeoutSeconds);
                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                        try {
                                keycloak.logout();
                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                        System.out.println("Exiting...");
                        System.exit(0);
                });
        }
}
```

#### 2.1.13. Security Context {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/adapter-context.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/adapter-context.adoc)

The `KeycloakSecurityContext` interface is available if you need to access to the tokens directly. This could be useful if you want to retrieve additional details from the token (such as user profile information) or you want to invoke a RESTful service that is protected by Keycloak.

In servlet environments it is available in secured invocations as an attribute in HttpServletRequest:

```
httpServletRequest
    .getAttribute(KeycloakSecurityContext.class.getName());
```

Or, it is available in insecured requests in the HttpSession:

```
httpServletRequest.getSession()
    .getAttribute(KeycloakSecurityContext.class.getName());
```

#### 2.1.14. Error Handling {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/adapter_error_handling.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/adapter_error_handling.adoc)

Keycloak has some error handling facilities for servlet based client adapters. When an error is encountered in authentication, Keycloak will call `HttpServletResponse.sendError()`. You can set up an error-page within your `web.xml` file to handle the error however you want. Keycloak can throw 400, 401, 403, and 500 errors.

```
<error-page>
    <error-code>403</error-code>
    <location>/ErrorHandler</location>
</error-page>
```

Keycloak also sets a `HttpServletRequest` attribute that you can retrieve. The attribute name is `org.keycloak.adapters.spi.AuthenticationError`, which should be casted to `org.keycloak.adapters.OIDCAuthenticationError`.

For example:

```
import org.keycloak.adapters.OIDCAuthenticationError;
import org.keycloak.adapters.OIDCAuthenticationError.Reason;
...

OIDCAuthenticationError error = (OIDCAuthenticationError) httpServletRequest
    .getAttribute('org.keycloak.adapters.spi.AuthenticationError');

Reason reason = error.getReason();
System.out.println(reason.name());
```

#### 2.1.15. Logout {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/logout.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/logout.adoc)

You can log out of a web application in multiple ways. For Java EE servlet containers, you can call `HttpServletRequest.logout()`. For other browser applications, you can redirect the browser to`http://auth-server/auth/realms/{realm-name}/protocol/openid-connect/logout?redirect_uri=encodedRedirectUri`, which logs you out if you have an SSO session with your browser.

When using the `HttpServletRequest.logout()` option the adapter executes a back-channel POST call against the Keycloak server passing the refresh token. If the method is executed from an unprotected page (a page that does not check for a valid token) the refresh token can be unavailable and, in that case, the adapter skips the call. For this reason, using a protected page to execute `HttpServletRequest.logout()` is recommended so that current tokens are always taken into account and an interaction with the Keycloak server is performed if needed.

If you want to avoid logging out of an external identity provider as part of the logout process, you can supply the parameter `initiating_idp`, with the value being the identity (alias) of the identity provider in question. This is useful when the logout endpoint is invoked as part of single logout initiated by the external identity provider.

#### 2.1.16. Parameters Forwarding {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/params_forwarding.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/params_forwarding.adoc)

The Keycloak initial authorization endpoint request has support for various parameters. Most of the parameters are described in [OIDC specification](https://openid.net/specs/openid-connect-core-1_0.html#AuthorizationEndpoint). Some parameters are added automatically by the adapter based on the adapter configuration. However, there are also a few parameters that can be added on a per-invocation basis. When you open the secured application URI, the particular parameter will be forwarded to the Keycloak authorization endpoint.

For example, if you request an offline token, then you can open the secured application URI with the `scope` parameter like:

```
http://myappserver/mysecuredapp?scope=offline_access
```

and the parameter `scope=offline_access` will be automatically forwarded to the Keycloak authorization endpoint.

The supported parameters are:

- scope - Use a space-delimited list of scopes. A space-delimited list typically references [Client scopes](https://www.keycloak.org/docs/6.0/server_admin/#_client_scopes) defined on particular client. Note that the scope `openid` will be always be added to the list of scopes by the adapter. For example, if you enter the scope options `address phone`, then the request to Keycloak will contain the scope parameter `scope=openid address phone`.
- prompt - Keycloak supports these settings:
  - `login` - SSO will be ignored and the Keycloak login page will be always shown, even if the user is already authenticated
  - `consent` - Applicable only for the clients with `Consent Required`. If it is used, the Consent page will always be displayed, even if the user previously granted consent to this client.
  - `none` - The login page will never be shown; instead the user will be redirected to the application, with an error if the user is not yet authenticated. This setting allows you to create a filter/interceptor on the application side and show a custom error page to the user. See more details in the specification.
- max_age - Used only if a user is already authenticated. Specifies maximum permitted time for the authentication to persist, measured from when the user authenticated. If user is authenticated longer than `maxAge`, the SSO is ignored and he must re-authenticate.
- login_hint - Used to pre-fill the username/email field on the login form.
- kc_idp_hint - Used to tell Keycloak to skip showing login page and automatically redirect to specified identity provider instead. More info in the [Identity Provider documentation](https://www.keycloak.org/docs/6.0/server_admin/#_client_suggested_idp).

Most of the parameters are described in the [OIDC specification](https://openid.net/specs/openid-connect-core-1_0.html#AuthorizationEndpoint). The only exception is parameter `kc_idp_hint`, which is specific to Keycloak and contains the name of the identity provider to automatically use. For more information see the `Identity Brokering` section in [Server Administration Guide](https://www.keycloak.org/docs/6.0/server_admin/).

|      | If you open the URL using the attached parameters, the adapter will not redirect you to Keycloak if you are already authenticated in the application. For example, opening http://myappserver/mysecuredapp?prompt=login will not automatically redirect you to the Keycloak login page if you are already authenticated to the application `mysecredapp` . This behavior may be changed in the future. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

#### 2.1.17. Client Authentication {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/client-authentication.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/client-authentication.adoc)

When a confidential OIDC client needs to send a backchannel request (for example, to exchange code for the token, or to refresh the token) it needs to authenticate against the Keycloak server. By default, there are three ways to authenticate the client: client ID and client secret, client authentication with signed JWT, or client authentication with signed JWT using client secret.

##### Client ID and Client Secret {#}

This is the traditional method described in the OAuth2 specification. The client has a secret, which needs to be known to both the adapter (application) and the Keycloak server. You can generate the secret for a particular client in the Keycloak administration console, and then paste this secret into the `keycloak.json` file on the application side:

```
"credentials": {
    "secret": "19666a4f-32dd-4049-b082-684c74115f28"
}
```

##### Client Authentication with Signed JWT {#}

This is based on the [RFC7523](https://tools.ietf.org/html/rfc7523) specification. It works this way:

- The client must have the private key and certificate. For Keycloak this is available through the traditional `keystore` file, which is either available on the client application’s classpath or somewhere on the file system.
- Once the client application is started, it allows to download its public key in [JWKS](https://self-issued.info/docs/draft-ietf-jose-json-web-key.html) format using a URL such as http://myhost.com/myapp/k_jwks, assuming that http://myhost.com/myapp is the base URL of your client application. This URL can be used by Keycloak (see below).
- During authentication, the client generates a JWT token and signs it with its private key and sends it to Keycloak in the particular backchannel request (for example, code-to-token request) in the `client_assertion` parameter.
- Keycloak must have the public key or certificate of the client so that it can verify the signature on JWT. In Keycloak you need to configure client credentials for your client. First you need to choose `Signed JWT` as the method of authenticating your client in the tab `Credentials` in administration console. Then you can choose to either:
  - Configure the JWKS URL where Keycloak can download the client’s public keys. This can be a URL such as http://myhost.com/myapp/k_jwks (see details above). This option is the most flexible, since the client can rotate its keys anytime and Keycloak then always downloads new keys when needed without needing to change the configuration. More accurately, Keycloak downloads new keys when it sees the token signed by an unknown `kid` (Key ID).
  - Upload the client’s public key or certificate, either in PEM format, in JWK format, or from the keystore. With this option, the public key is hardcoded and must be changed when the client generates a new key pair. You can even generate your own keystore from the Keycloak admininstration console if you don’t have your own available. For more details on how to set up the Keycloak administration console see [Server Administration Guide](https://www.keycloak.org/docs/6.0/server_admin/).

For set up on the adapter side you need to have something like this in your `keycloak.json` file:

```
"credentials": {
  "jwt": {
    "client-keystore-file": "classpath:keystore-client.jks",
    "client-keystore-type": "JKS",
    "client-keystore-password": "storepass",
    "client-key-password": "keypass",
    "client-key-alias": "clientkey",
    "token-expiration": 10
  }
}
```

With this configuration, the keystore file `keystore-client.jks` must be available on classpath in your WAR. If you do not use the prefix `classpath:` you can point to any file on the file system where the client application is running.

For inspiration, you can take a look at the examples distribution into the main demo example into the `product-portal`application.

##### Client Authentication with Signed JWT using Client Secret {#}

This is the same as Client Authentication with Signed JWT except for using the client secret instead of the private key and certificate.

The client has a secret, which needs to be known to both the adapter (application) and the Keycloak server. You need to choose `Signed JWT with Client Secret` as the method of authenticating your client in the tab `Credentials` in administration console, and then paste this secret into the `keycloak.json` file on the application side:

```
"credentials": {
  "secret-jwt": {
    "secret": "19666a4f-32dd-4049-b082-684c74115f28"
  }
}
```

##### Add Your Own Client Authentication Method {#}

You can add your own client authentication method as well. You will need to implement both client-side and server-side providers. For more details see the `Authentication SPI` section in [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/).

#### 2.1.18. Multi Tenancy {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/multi-tenancy.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/multi-tenancy.adoc)

Multi Tenancy, in our context, means that a single target application (WAR) can be secured with multiple Keycloak realms. The realms can be located one the same Keycloak instance or on different instances.

In practice, this means that the application needs to have multiple `keycloak.json` adapter configuration files.

You could have multiple instances of your WAR with different adapter configuration files deployed to different context-paths. However, this may be inconvenient and you may also want to select the realm based on something else than context-path.

Keycloak makes it possible to have a custom config resolver so you can choose what adapter config is used for each request.

To achieve this first you need to create an implementation of `org.keycloak.adapters.KeycloakConfigResolver`. For example:

```
package example;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;

public class PathBasedKeycloakConfigResolver implements KeycloakConfigResolver {

    @Override
    public KeycloakDeployment resolve(OIDCHttpFacade.Request request) {
        if (path.startsWith("alternative")) {
            KeycloakDeployment deployment = cache.get(realm);
            if (null == deployment) {
                InputStream is = getClass().getResourceAsStream("/tenant1-keycloak.json");
                return KeycloakDeploymentBuilder.build(is);
            }
        } else {
            InputStream is = getClass().getResourceAsStream("/default-keycloak.json");
            return KeycloakDeploymentBuilder.build(is);
        }
    }

}
```

You also need to configure which `KeycloakConfigResolver` implementation to use with the `keycloak.config.resolver`context-param in your `web.xml`:

```
<web-app>
    ...
    <context-param>
        <param-name>keycloak.config.resolver</param-name>
        <param-value>example.PathBasedKeycloakConfigResolver</param-value>
    </context-param>
</web-app>
```

#### 2.1.19. Application Clustering {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/java/application-clustering.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/java/application-clustering.adoc)

This chapter is related to supporting clustered applications deployed to JBoss EAP, WildFly and JBoss AS.

There are a few options available depending on whether your application is:

- Stateless or stateful
- Distributable (replicated http session) or non-distributable
- Relying on sticky sessions provided by load balancer
- Hosted on same domain as Keycloak

Dealing with clustering is not quite as simple as for a regular application. Mainly due to the fact that both the browser and the server-side application sends requests to Keycloak, so it’s not as simple as enabling sticky sessions on your load balancer.

##### Stateless token store {#}

By default, the web application secured by Keycloak uses the HTTP session to store security context. This means that you either have to enable sticky sessions or replicate the HTTP session.

As an alternative to storing the security context in the HTTP session the adapter can be configured to store this in a cookie instead. This is useful if you want to make your application stateless or if you don’t want to store the security context in the HTTP session.

To use the cookie store for saving the security context, edit your applications `WEB-INF/keycloak.json` and add:

```
"token-store": "cookie"
```

|      | The default value for `token-store` is `session`, which stores the security context in the HTTP session. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

One limitation of using the cookie store is that the whole security context is passed in the cookie for every HTTP request. This may impact performance.

Another small limitation is limited support for Single-Sign Out. It works without issues if you init servlet logout (HttpServletRequest.logout) from the application itself as the adapter will delete the KEYCLOAK_ADAPTER_STATE cookie. However, back-channel logout initialized from a different application isn’t propagated by Keycloak to applications using cookie store. Hence it’s recommended to use a short value for the access token timeout (for example 1 minute).

|      | Some load balancers do not allow any configuration of the sticky session cookie name or contents, such as Amazon ALB. For these, it is recommended to set the `shouldAttachRoute` option to `false`. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

##### Relative URI optimization {#}

In deployment scenarios where Keycloak and the application is hosted on the same domain (through a reverse proxy or load balancer) it can be convenient to use relative URI options in your client configuration.

With relative URIs the URI is resolved as relative to the URL used to access Keycloak.

For example if the URL to your application is `https://acme.org/myapp` and the URL to Keycloak is `https://acme.org/auth`, then you can use the redirect-uri `/myapp` instead of `https://acme.org/myapp`.

##### Admin URL configuration {#}

Admin URL for a particular client can be configured in the Keycloak Administration Console. It’s used by the Keycloak server to send backend requests to the application for various tasks, like logout users or push revocation policies.

For example the way backchannel logout works is:

1. User sends logout request from one application
2. The application sends logout request to Keycloak
3. The Keycloak server invalidates the user session
4. The Keycloak server then sends a backchannel request to application with an admin url that are associated with the session
5. When an application receives the logout request it invalidates the corresponding HTTP session

If admin URL contains `${application.session.host}` it will be replaced with the URL to the node associated with the HTTP session.

##### Registration of application nodes {#}

The previous section describes how Keycloak can send logout request to node associated with a specific HTTP session. However, in some cases admin may want to propagate admin tasks to all registered cluster nodes, not just one of them. For example to push a new not before policy to the application or to logout all users from the application.

In this case Keycloak needs to be aware of all application cluster nodes, so it can send the event to all of them. To achieve this, we support auto-discovery mechanism:

1. When a new application node joins the cluster, it sends a registration request to the Keycloak server
2. The request may be re-sent to Keycloak in configured periodic intervals
3. If the Keycloak server doesn’t receive a re-registration request within a specified timeout then it automatically unregisters the specific node
4. The node is also unregistered in Keycloak when it sends an unregistration request, which is usually during node shutdown or application undeployment. This may not work properly for forced shutdown when undeployment listeners are not invoked, which results in the need for automatic unregistration

Sending startup registrations and periodic re-registration is disabled by default as it’s only required for some clustered applications.

To enable the feature edit the `WEB-INF/keycloak.json` file for your application and add:

```
"register-node-at-startup": true,
"register-node-period": 600,
```

This means the adapter will send the registration request on startup and re-register every 10 minutes.

In the Keycloak Administration Console you can specify the maximum node re-registration timeout (should be larger than *register-node-period* from the adapter configuration). You can also manually add and remove cluster nodes in through the Adminstration Console, which is useful if you don’t want to rely on the automatic registration feature or if you want to remove stale application nodes in the event your not using the automatic unregistration feature.

##### Refresh token in each request {#}

By default the application adapter will only refresh the access token when it’s expired. However, you can also configure the adapter to refresh the token on every request. This may have a performance impact as your application will send more requests to the Keycloak server.

To enable the feature edit the `WEB-INF/keycloak.json` file for your application and add:

```
"always-refresh-token": true
```

|      | This may have a significant impact on performance. Only enable this feature if you can’t rely on backchannel messages to propagate logout and not before policies. Another thing to consider is that by default access tokens has a short expiration so even if logout is not propagated the token will expire within minutes of the logout. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 2.2. JavaScript Adapter {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/javascript-adapter.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/javascript-adapter.adoc)

Keycloak comes with a client-side JavaScript library that can be used to secure HTML5/JavaScript applications. The JavaScript adapter has built-in support for Cordova applications.

The library can be retrieved directly from the Keycloak server at `/auth/js/keycloak.js` and is also distributed as a ZIP archive.

A best practice is to load the JavaScript adapter directly from Keycloak Server as it will automatically be updated when you upgrade the server. If you copy the adapter to your web application instead, make sure you upgrade the adapter only after you have upgraded the server.

One important thing to note about using client-side applications is that the client has to be a public client as there is no secure way to store client credentials in a client-side application. This makes it very important to make sure the redirect URIs you have configured for the client are correct and as specific as possible.

To use the JavaScript adapter you must first create a client for your application in the Keycloak Administration Console. Make sure `public` is selected for `Access Type`.

You also need to configure valid redirect URIs and valid web origins. Be as specific as possible as failing to do so may result in a security vulnerability.

Once the client is created click on the `Installation` tab select `Keycloak OIDC JSON` for `Format Option` then click `Download`. The downloaded `keycloak.json` file should be hosted on your web server at the same location as your HTML pages.

Alternatively, you can skip the configuration file and manually configure the adapter.

The following example shows how to initialize the JavaScript adapter:

```
<head>
    <script src="keycloak.js"></script>
    <script>
        var keycloak = Keycloak();
        keycloak.init().success(function(authenticated) {
            alert(authenticated ? 'authenticated' : 'not authenticated');
        }).error(function() {
            alert('failed to initialize');
        });
    </script>
</head>
```

If the `keycloak.json` file is in a different location you can specify it:

```
var keycloak = Keycloak('http://localhost:8080/myapp/keycloak.json');
```

Alternatively, you can pass in a JavaScript object with the required configuration instead:

```
var keycloak = Keycloak({
    url: 'http://keycloak-server/auth',
    realm: 'myrealm',
    clientId: 'myapp'
});
```

By default to authenticate you need to call the `login` function. However, there are two options available to make the adapter automatically authenticate. You can pass `login-required` or `check-sso` to the init function. `login-required`will authenticate the client if the user is logged-in to Keycloak or display the login page if not. `check-sso` will only authenticate the client if the user is already logged-in, if the user is not logged-in the browser will be redirected back to the application and remain unauthenticated.

To enable `login-required` set `onLoad` to `login-required` and pass to the init method:

```
keycloak.init({ onLoad: 'login-required' })
```

After the user is authenticated the application can make requests to RESTful services secured by Keycloak by including the bearer token in the `Authorization` header. For example:

```
var loadData = function () {
    document.getElementById('username').innerText = keycloak.subject;

    var url = 'http://localhost:8080/restful-service';

    var req = new XMLHttpRequest();
    req.open('GET', url, true);
    req.setRequestHeader('Accept', 'application/json');
    req.setRequestHeader('Authorization', 'Bearer ' + keycloak.token);

    req.onreadystatechange = function () {
        if (req.readyState == 4) {
            if (req.status == 200) {
                alert('Success');
            } else if (req.status == 403) {
                alert('Forbidden');
            }
        }
    }

    req.send();
};
```

One thing to keep in mind is that the access token by default has a short life expiration so you may need to refresh the access token prior to sending the request. You can do this by the `updateToken` method. The `updateToken` method returns a promise object which makes it easy to invoke the service only if the token was successfully refreshed and for example display an error to the user if it wasn’t. For example:

```
keycloak.updateToken(30).success(function() {
    loadData();
}).error(function() {
    alert('Failed to refresh token');
});
```

#### 2.2.1. Session Status iframe {#}

By default, the JavaScript adapter creates a hidden iframe that is used to detect if a Single-Sign Out has occurred. This does not require any network traffic, instead the status is retrieved by looking at a special status cookie. This feature can be disabled by setting `checkLoginIframe: false` in the options passed to the `init` method.

You should not rely on looking at this cookie directly. Its format can change and it’s also associated with the URL of the Keycloak server, not your application.

#### 2.2.2. Implicit and Hybrid Flow {#}

By default, the JavaScript adapter uses the [Authorization Code](https://openid.net/specs/openid-connect-core-1_0.html#CodeFlowAuth) flow.

With this flow the Keycloak server returns an authorization code, not an authentication token, to the application. The JavaScript adapter exchanges the `code` for an access token and a refresh token after the browser is redirected back to the application.

Keycloak also supports the [Implicit](https://openid.net/specs/openid-connect-core-1_0.html#ImplicitFlowAuth) flow where an access token is sent immediately after successful authentication with Keycloak. This may have better performance than standard flow, as there is no additional request to exchange the code for tokens, but it has implications when the access token expires.

However, sending the access token in the URL fragment can be a security vulnerability. For example the token could be leaked through web server logs and or browser history.

To enable implicit flow, you need to enable the `Implicit Flow Enabled` flag for the client in the Keycloak Administration Console. You also need to pass the parameter `flow` with value `implicit` to `init` method:

```
keycloak.init({ flow: 'implicit' })
```

One thing to note is that only an access token is provided and there is no refresh token. This means that once the access token has expired the application has to do the redirect to the Keycloak again to obtain a new access token.

Keycloak also supports the [Hybrid](https://openid.net/specs/openid-connect-core-1_0.html#HybridFlowAuth) flow.

This requires the client to have both the `Standard Flow Enabled` and `Implicit Flow Enabled` flags enabled in the admin console. The Keycloak server will then send both the code and tokens to your application. The access token can be used immediately while the code can be exchanged for access and refresh tokens. Similar to the implicit flow, the hybrid flow is good for performance because the access token is available immediately. But, the token is still sent in the URL, and the security vulnerability mentioned earlier may still apply.

One advantage in the Hybrid flow is that the refresh token is made available to the application.

For the Hybrid flow, you need to pass the parameter `flow` with value `hybrid` to the `init` method:

```
keycloak.init({ flow: 'hybrid' })
```

#### 2.2.3. Hybrid Apps with Cordova   {#}

Keycloak support hybrid mobile apps developed with [Apache Cordova](https://cordova.apache.org/). The Javascript adapter has two modes for this: `cordova` and `cordova-native`:

The default is cordova, which the adapter will automatically select if no adapter type has been configured and window.cordova is present. When logging in, it will open an [InApp Browser](https://cordova.apache.org/docs/en/latest/reference/cordova-plugin-inappbrowser/) that lets the user interact with Keycloak and afterwards returns to the app by redirecting to `http://localhost`. Because of this, you must whitelist this URL as a valid redirect-uri in the client configuration section of the Administration Console.

While this mode is easy to setup, it also has some disadvantages: * The InApp-Browser is a browser embedded in the app and is not the phone’s default browser. Therefore it will have different settings and stored credentials will not be available. * The InApp-Browser might also be slower, especially when rendering more complex themes. * There are security concerns to consider, before using this mode, such as that it is possible for the app to gain access to the credentials of the user, as it has full control of the browser rendering the login page, so do not allow its use in apps you do not trust.

Use this example app to help you get started: <https://github.com/keycloak/keycloak/tree/master/examples/cordova>

The alternative mode `cordova-nativei` takes a different approach. It opens the login page using the system’s browser. After the user has authenticated, the browser redirects back into the app using a special URL. From there, the Keycloak adapter can finish the login by reading the code or token from the URL.

You can activate the native mode by passing the adapter type `cordova-native` to the `init` method:

```
keycloak.init({ adapter: 'cordova-native' })
```

This adapter required two additional plugins:

- [cordova-plugin-browsertab](https://github.com/google/cordova-plugin-browsertab): allows the app to open webpages in the system’s browser
- [cordova-plugin-deeplinks](https://github.com/e-imaxina/cordova-plugin-deeplinks): allow the browser to redirect back to your app by special URLs

The technical details for linking to an app differ on each plattform and special setup is needed. Please refer to the Android and iOS sections of the [deeplinks plugin documentation](https://github.com/e-imaxina/cordova-plugin-deeplinks/blob/master/README.md) for further instructions.

There are different kinds of links for opening apps: custom schemes (i.e. `myapp://login` or `android-app://com.example.myapp/https/example.com/login`) and [Universal Links (iOS)](https://developer.apple.com/ios/universal-links/)) / [Deep Links (Android)](https://developer.android.com/training/app-links/deep-linking). While the former are easier to setup and tend to work more reliably, the later offer extra security as they are unique and only the owner of a domain can register them. Custom-URLs are deprecated on iOS. We recommend that you use universal links, combined with a fallback site with a custom-url link on it for best reliability.

Furthermore, we recommend the following steps to improve compatibility with the Keycloak Adapter:

- Universal Links on iOS seem to work more reliably with `response-mode` set to `query`
- To prevent Android from opening a new instance of your app on redirect add the following snippet to `config.xml`:

```
<preference name="AndroidLaunchMode" value="singleTask" />
```

There is an example app that shows how to use the native-mode: <https://github.com/keycloak/keycloak/tree/master/examples/cordova-native>

#### 2.2.4. Earlier Browsers {#}

The JavaScript adapter depends on Base64 (window.btoa and window.atob), HTML5 History API and optionally the Promise API. If you need to support browsers that do not have these available (for example, IE9) you need to add polyfillers.

Example polyfill libraries:

- Base64 - <https://github.com/davidchambers/Base64.js>
- HTML5 History - <https://github.com/devote/HTML5-History-API>
- Promise - <https://github.com/stefanpenner/es6-promise>

#### 2.2.5. JavaScript Adapter Reference {#}

##### Constructor {#}

```
new Keycloak();
new Keycloak('http://localhost/keycloak.json');
new Keycloak({ url: 'http://localhost/auth', realm: 'myrealm', clientId: 'myApp' });
```

##### Properties {#}

- authenticated

  Is `true` if the user is authenticated, `false` otherwise.

- token

  The base64 encoded token that can be sent in the `Authorization` header in requests to services.

- tokenParsed

  The parsed token as a JavaScript object.

- subject

  The user id.

- idToken

  The base64 encoded ID token.

- idTokenParsed

  The parsed id token as a JavaScript object.

- realmAccess

  The realm roles associated with the token.

- resourceAccess

  The resource roles associated with the token.

- refreshToken

  The base64 encoded refresh token that can be used to retrieve a new token.

- refreshTokenParsed

  The parsed refresh token as a JavaScript object.

- timeSkew

  The estimated time difference between the browser time and the Keycloak server in seconds. This value is just an estimation, but is accurate enough when determining if a token is expired or not.

- responseMode

  Response mode passed in init (default value is fragment).

- flow

  Flow passed in init.

- adapter

  Allows you to override the way that redirects and other browser-related functions will be handled by the library. Available options:"default" - the library uses the browser api for redirects (this is the default)"cordova" - the library will try to use the InAppBrowser cordova plugin to load keycloak login/registration pages (this is used automatically when the library is working in a cordova ecosystem)"cordova-native" - the library tries to open the login and registration page using the phone’s system browser using the BrowserTabs cordova plugin. This requires extra setup for redirecting back to the app (see [Hybrid Apps with Cordova](https://www.keycloak.org/docs/latest/securing_apps/index.html#hybrid-apps-with-cordova)).custom - allows you to implement a custom adapter (only for advanced use cases)

- responseType

  Response type sent to Keycloak with login requests. This is determined based on the flow value used during initialization, but can be overridden by setting this value.

##### Methods {#}

###### init(options) {#}

Called to initialize the adapter.

Options is an Object, where:

- onLoad - Specifies an action to do on load. Supported values are 'login-required' or 'check-sso'.
- token - Set an initial value for the token.
- refreshToken - Set an initial value for the refresh token.
- idToken - Set an initial value for the id token (only together with token or refreshToken).
- timeSkew - Set an initial value for skew between local time and Keycloak server in seconds (only together with token or refreshToken).
- checkLoginIframe - Set to enable/disable monitoring login state (default is true).
- checkLoginIframeInterval - Set the interval to check login state (default is 5 seconds).
- responseMode - Set the OpenID Connect response mode send to Keycloak server at login request. Valid values are query or fragment . Default value is fragment, which means that after successful authentication will Keycloak redirect to javascript application with OpenID Connect parameters added in URL fragment. This is generally safer and recommended over query.
- flow - Set the OpenID Connect flow. Valid values are standard, implicit or hybrid.
- promiseType - If set to `native` all methods returning a promise will return a native JavaScript promise. If not set will return Keycloak specific promise objects.

Returns promise to set functions to be invoked on success or error.

###### login(options) {#}

Redirects to login form on (options is an optional object with redirectUri and/or prompt fields).

Options is an Object, where:

- redirectUri - Specifies the uri to redirect to after login.
- prompt - This parameter allows to slightly customize the login flow on the Keycloak server side. For example enforce displaying the login screen in case of value `login`. See [Parameters Forwarding Section](https://www.keycloak.org/docs/latest/securing_apps/index.html#_params_forwarding) for the details and all the possible values of the `prompt` parameter.
- maxAge - Used just if user is already authenticated. Specifies maximum time since the authentication of user happened. If user is already authenticated for longer time than `maxAge`, the SSO is ignored and he will need to re-authenticate again.
- loginHint - Used to pre-fill the username/email field on the login form.
- scope - Used to forward the scope parameter to the Keycloak login endpoint. Use a space-delimited list of scopes. Those typically reference [Client scopes](https://www.keycloak.org/docs/6.0/server_admin/#_client_scopes) defined on particular client. Note that the scope `openid` will be always be added to the list of scopes by the adapter. For example, if you enter the scope options `address phone`, then the request to Keycloak will contain the scope parameter `scope=openid address phone`.
- idpHint - Used to tell Keycloak to skip showing the login page and automatically redirect to the specified identity provider instead. More info in the [Identity Provider documentation](https://www.keycloak.org/docs/6.0/server_admin/#_client_suggested_idp).
- action - If value is 'register' then user is redirected to registration page, otherwise to login page.
- locale - Sets the 'ui_locales' query param in compliance with section 3.1.2.1 of the OIDC 1.0 specification.
- kcLocale - Specifies the desired Keycloak locale for the UI. This differs from the locale param in that it tells the Keycloak server to set a cookie and update the user’s profile to a new preferred locale.
- cordovaOptions - Specifies the arguments that are passed to the Cordova in-app-browser (if applicable). Options `hidden`and `location` are not affected by these arguments. All available options are defined at <https://cordova.apache.org/docs/en/latest/reference/cordova-plugin-inappbrowser/>. Example of use: `{ zoom: "no", hardwareback: "yes" }`;

###### createLoginUrl(options) {#}

Returns the URL to login form on (options is an optional object with redirectUri and/or prompt fields).

Options is an Object, which supports same options like the function `login` .

###### logout(options) {#}

Redirects to logout.

Options is an Object, where:

- redirectUri - Specifies the uri to redirect to after logout.

###### createLogoutUrl(options) {#}

Returns the URL to logout the user.

Options is an Object, where:

- redirectUri - Specifies the uri to redirect to after logout.

###### register(options) {#}

Redirects to registration form. Shortcut for login with option action = 'register'

Options are same as for the login method but 'action' is set to 'register'

###### createRegisterUrl(options) {#}

Returns the url to registration page. Shortcut for createLoginUrl with option action = 'register'

Options are same as for the createLoginUrl method but 'action' is set to 'register'

###### accountManagement() {#}

Redirects to the Account Management Console.

###### createAccountUrl() {#}

Returns the URL to the Account Management Console.

###### hasRealmRole(role) {#}

Returns true if the token has the given realm role.

###### hasResourceRole(role, resource) {#}

Returns true if the token has the given role for the resource (resource is optional, if not specified clientId is used).

###### loadUserProfile() {#}

Loads the users profile.

Returns promise to set functions to be invoked if the profile was loaded successfully, or if the profile could not be loaded.

For example:

```
keycloak.loadUserProfile().success(function(profile) {
        alert(JSON.stringify(profile, null, "  "));
    }).error(function() {
        alert('Failed to load user profile');
    });
```

###### isTokenExpired(minValidity) {#}

Returns true if the token has less than minValidity seconds left before it expires (minValidity is optional, if not specified 0 is used).

###### updateToken(minValidity) {#}

If the token expires within minValidity seconds (minValidity is optional, if not specified 5 is used) the token is refreshed. If the session status iframe is enabled, the session status is also checked.

Returns promise to set functions that can be invoked if the token is still valid, or if the token is no longer valid. For example:

```
keycloak.updateToken(5).success(function(refreshed) {
        if (refreshed) {
            alert('Token was successfully refreshed');
        } else {
            alert('Token is still valid');
        }
    }).error(function() {
        alert('Failed to refresh the token, or the session has expired');
    });
```

###### clearToken() {#}

Clear authentication state, including tokens. This can be useful if application has detected the session was expired, for example if updating token fails.

Invoking this results in onAuthLogout callback listener being invoked.

##### Callback Events {#}

The adapter supports setting callback listeners for certain events.

For example:

```
keycloak.onAuthSuccess = function() { alert('authenticated'); }
```

The available events are:

- onReady(authenticated) - Called when the adapter is initialized.
- onAuthSuccess - Called when a user is successfully authenticated.
- onAuthError - Called if there was an error during authentication.
- onAuthRefreshSuccess - Called when the token is refreshed.
- onAuthRefreshError - Called if there was an error while trying to refresh the token.
- onAuthLogout - Called if the user is logged out (will only be called if the session status iframe is enabled, or in Cordova mode).
- onTokenExpired - Called when the access token is expired. If a refresh token is available the token can be refreshed with updateToken, or in cases where it is not (that is, with implicit flow) you can redirect to login screen to obtain a new access token.

### 2.3. Node.js Adapter {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/nodejs-adapter.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/nodejs-adapter.adoc)

Keycloak provides a Node.js adapter built on top of [Connect](https://github.com/senchalabs/connect) to protect server-side JavaScript apps - the goal was to be flexible enough to integrate with frameworks like [Express.js](https://expressjs.com/).

The library can be downloaded directly from [Keycloak organization](https://www.npmjs.com/package/keycloak-connect) and the source is available at [GitHub](https://github.com/keycloak/keycloak-nodejs-connect).

To use the Node.js adapter, first you must create a client for your application in the Keycloak Administration Console. The adapter supports public, confidential, and bearer-only access type. Which one to choose depends on the use-case scenario.

Once the client is created click the `Installation` tab, select `Keycloak OIDC JSON` for `Format Option`, and then click `Download`. The downloaded `keycloak.json` file should be at the root folder of your project.

#### 2.3.1. Installation {#}

Assuming you’ve already installed [Node.js](https://nodejs.org/), create a folder for your application:

```
mkdir myapp && cd myapp
```

Use `npm init` command to create a `package.json` for your application. Now add the Keycloak connect adapter in the dependencies list:

```
    "dependencies": {
        "keycloak-connect": "6.0.1"
    }
```

#### 2.3.2. Usage {#}

- Instantiate a Keycloak class

  The `Keycloak` class provides a central point for configuration and integration with your application. The simplest creation involves no arguments.

```
    var session = require('express-session');
    var Keycloak = require('keycloak-connect');

    var memoryStore = new session.MemoryStore();
    var keycloak = new Keycloak({ store: memoryStore });
```

By default, this will locate a file named `keycloak.json` alongside the main executable of your application to initialize keycloak-specific settings (public key, realm name, various URLs). The `keycloak.json` file is obtained from the Keycloak Admin Console.

Instantiation with this method results in all of the reasonable defaults being used. As alternative, it’s also possible to provide a configuration object, rather than the `keycloak.json` file:

```
    let kcConfig = {
        clientId: 'myclient',
        bearerOnly: true,
        serverUrl: 'http://localhost:8080/auth',
        realm: 'myrealm',
        realmPublicKey: 'MIIBIjANB...'
    };

    let keycloak = new Keycloak({ store: memoryStore }, kcConfig);
```

Applications can also redirect users to their preferred identity provider by using:

```
    let keycloak = new Keycloak({ store: memoryStore, idpHint: myIdP }, kcConfig);
```

- Configuring a web session store

  If you want to use web sessions to manage server-side state for authentication, you need to initialize the `Keycloak(…)`with at least a `store` parameter, passing in the actual session store that `express-session` is using.

```
    var session = require('express-session');
    var memoryStore = new session.MemoryStore();

    var keycloak = new Keycloak({ store: memoryStore });
```

- Passing a custom scope value

  By default, the scope value `openid` is passed as a query parameter to Keycloak’s login URL, but you can add an additional custom value:

```
    var keycloak = new Keycloak({ scope: 'offline_access' });
```

#### 2.3.3. Installing Middleware {#}

Once instantiated, install the middleware into your connect-capable app:

```
    var app = express();

    app.use( keycloak.middleware() );
```

#### 2.3.4. Checking Authentication {#}

To check that a user is authenticated before accessing a resource, simply use `keycloak.checkSso()`. It will only authenticate if the user is already logged-in. If the user is not logged-in, the browser will be redirected back to the originally-requested URL and remain unauthenticated:

```
    app.get( '/check-sso', keycloak.checkSso(), checkSsoHandler );
```

#### 2.3.5. Protecting Resources {#}

- Simple authentication

  To enforce that a user must be authenticated before accessing a resource, simply use a no-argument version of `keycloak.protect()`:

```
    app.get( '/complain', keycloak.protect(), complaintHandler );
```

- Role-based authorization

  To secure a resource with an application role for the current app:

```
    app.get( '/special', keycloak.protect('special'), specialHandler );
```

To secure a resource with an application role for a **different** app:

```
    app.get( '/extra-special', keycloak.protect('other-app:special'), extraSpecialHandler );
```

To secure a resource with a realm role:

```
    app.get( '/admin', keycloak.protect( 'realm:admin' ), adminHandler );
```

- Advanced authorization

  To secure resources based on parts of the URL itself, assuming a role exists for each section:

```
    function protectBySection(token, request) {
      return token.hasRole( request.params.section );
    }

    app.get( '/:section/:page', keycloak.protect( protectBySection ), sectionHandler );
```

#### 2.3.6. Additional URLs {#}

- Explicit user-triggered logout

  By default, the middleware catches calls to `/logout` to send the user through a Keycloak-centric logout workflow. This can be changed by specifying a `logout` configuration parameter to the `middleware()` call:

```
    app.use( keycloak.middleware( { logout: '/logoff' } ));
```

- Keycloak Admin Callbacks

  Also, the middleware supports callbacks from the Keycloak console to log out a single session or all sessions. By default, these type of admin callbacks occur relative to the root URL of `/` but can be changed by providing an `admin` parameter to the `middleware()` call:

```
    app.use( keycloak.middleware( { admin: '/callbacks' } );
```

### 2.4. Keycloak Gatekeeper {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/keycloak-gatekeeper.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/keycloak-gatekeeper.adoc)

Keycloak provides a Go programming language adapter for use with OpenID Connect (OIDC) that supports both access tokens in a browser cookie or bearer tokens.

This documentation details how to build and configure keycloak-gatekeeper followed by details of how to use each of its features.

For further information, see the included help file which includes a full list of commands and switches. View the file by entering the following at the command line (modify the location to match where you install keycloak-gatekeeper):

```
    $ bin/keycloak-gatekeeper help
```

#### 2.4.1. Building {#}

Prerequisites

- Golang must be installed.
- Make must be installed.

Procedure

- Run `make dep-install` to install all needed dependencies.
- Run `make test` to run the included tests.
- Run `make` to build the project. You can instead use `make static` if you prefer to build a binary that includes within it all of the required dependencies.

|      | You can also build via docker container: `make docker-build`. A Docker image is available at <https://hub.docker.com/r/keycloak/keycloak-gatekeeper/>. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

#### 2.4.2. Configuration options {#}

Configuration can come from a yaml/json file or by using command line options. Here is a list of options.

```
# is the url for retrieve the OpenID configuration - normally the <server>/auth/realm/<realm_name> {#}
discovery-url: https://keycloak.example.com/auth/realms/<REALM_NAME>
# the client id for the 'client' application {#}
client-id: <CLIENT_ID>
# the secret associated to the 'client' application {#}
client-secret: <CLIENT_SECRET>
# the interface definition you wish the proxy to listen, all interfaces is specified as ':<port>', unix sockets as unix://<REL_PATH>|</ABS PATH> {#}
listen: 127.0.0.1:3000
# whether to enable refresh tokens {#}
enable-refresh-tokens: true
# the location of a certificate you wish the proxy to use for TLS support {#}
tls-cert:
# the location of a private key for TLS {#}
tls-private-key:
# the redirection url, essentially the site url, note: /oauth/callback is added at the end {#}
redirection-url: http://127.0.0.1:3000
# the encryption key used to encode the session state {#}
encryption-key: <ENCRYPTION_KEY>
# the upstream endpoint which we should proxy request {#}
upstream-url: http://127.0.0.1:80
# additional scopes to add to add to the default (openid+email+profile) {#}
scopes:
- vpn-user
# a collection of resource i.e. urls that you wish to protect {#}
resources:
- uri: /admin/test
  # the methods on this url that should be protected, if missing, we assuming all {#}
  methods:
  - GET
  # a list of roles the user must have in order to access urls under the above {#}
  # If all you want is authentication ONLY, simply remove the roles array - the user must be authenticated but {#}
  # no roles are required {#}
  roles:
  - openvpn:vpn-user
  - openvpn:prod-vpn
  - test
- uri: /admin/*
  methods:
  - GET
  roles:
  - openvpn:vpn-user
  - openvpn:commons-prod-vpn
```

Options issued at the command line have a higher priority and will override or merge with options referenced in a config file. Examples of each style are shown here.

#### 2.4.3. Example usage and configuration {#}

Assuming you have some web service you wish protected by Keycloak:

- Create the **client** using the Keycloak GUI or CLI; the client protocol is **'openid-connect'**, access-type: **confidential**.
- Add a Valid Redirect URI of **http://127.0.0.1:3000/oauth/callback**.
- Grab the client id and client secret.
- Create the various roles under the client or existing clients for authorization purposes.

Here is an example configuration file.

```
client-id: <CLIENT_ID>
client-secret: <CLIENT_SECRET> # require for access_type: confidential
# Note the redirection-url is optional, it will default to the X-Forwarded-Proto / X-Forwarded-Host r the URL scheme and host not found {#}
discovery-url: https://keycloak.example.com/auth/realms/<REALM_NAME>
enable-default-deny: true
encryption_key: AgXa7xRcoClDEU0ZDSH4X0XhL5Qy2Z2j
listen: 127.0.0.1:3000
redirection-url: http://127.0.0.1:3000
upstream-url: http://127.0.0.1:80
resources:
- uri: /admin*
  methods:
  - GET
  roles:
  - client:test1
  - client:test2
  require-any-role: true
  groups:
  - admins
  - users
- uri: /backend*
  roles:
  - client:test1
- uri: /public/*
  white-listed: true
- uri: /favicon
  white-listed: true
- uri: /css/*
  white-listed: true
- uri: /img/*
  white-listed: true
headers:
  myheader1: value_1
  myheader2: value_2
```

Anything defined in a configuration file can also be configured using command line options, such as in this example.

```
bin/keycloak-gatekeeper \
    --discovery-url=https://keycloak.example.com/auth/realms/<REALM_NAME> \
    --client-id=<CLIENT_ID> \
    --client-secret=<SECRET> \
    --listen=127.0.0.1:3000 \ # unix sockets format unix://path
    --redirection-url=http://127.0.0.1:3000 \
    --enable-refresh-tokens=true \
    --encryption-key=AgXa7xRcoClDEU0ZDSH4X0XhL5Qy2Z2j \
    --upstream-url=http://127.0.0.1:80 \
    --enable-default-deny=true \
    --resources="uri=/admin*|roles=test1,test2" \
    --resources="uri=/backend*|roles=test1" \
    --resources="uri=/css/*|white-listed=true" \
    --resources="uri=/img/*|white-listed=true" \
    --resources="uri=/public/*|white-listed=true" \
    --headers="myheader1=value1" \
    --headers="myheader2=value2"
```

By default the roles defined on a resource perform a logical `AND` so all roles specified must be present in the claims, this behavior can be altered by the `require-any-role` option, however, so as long as one role is present the permission is granted.

#### 2.4.4. OpenID Provider Communication {#}

By default the communication with the OpenID provider is direct. If you wish, you can specify a forwarding proxy server in your configuration file:

```
openid-provider-proxy: http://proxy.example.com:8080
```

#### 2.4.5. HTTP routing {#}

By default all requests will be proxyed on to the upstream, if you wish to ensure all requests are authentication you can use this:

```
--resource=uri=/* # note, unless specified the method is assumed to be 'any|ANY'
```

The HTTP routing rules follow the guidelines from [chi](https://github.com/go-chi/chi#router-design). The ordering of the resources do not matter, the router will handle that for you.

#### 2.4.6. Session-only cookies {#}

By default the access and refresh cookies are session-only and disposed of on browser close; you can disable this feature using the `--enable-session-cookies` option.

#### 2.4.7. Forward-signing proxy {#}

Forward-signing provides a mechanism for authentication and authorization between services using tokens issued from the IdP. When operating in this mode the proxy will automatically acquire an access token (handling the refreshing or logins on your behalf) and tag outbound requests with a Authorization header. You can control which domains are tagged with the --forwarding-domains option. Note, this option use a **contains** comparison on domains. So, if you wanted to match all domains under *.svc.cluster.local you can use: --forwarding-domain=svc.cluster.local.

At present the service performs a login using oauth client_credentials grant type, so your IdP service must support direct (username/password) logins.

Example setup:

You have collection of micro-services which are permitted to speak to one another; you have already set up the credentials, roles, and clients in Keycloak, providing granular role controls over issue tokens.

```
- name: keycloak-gatekeeper
  image: quay.io/gambol99/keycloak-generic-adapter:latest
  args:
  - --enable-forwarding=true
  - --forwarding-username=projecta
  - --forwarding-password=some_password
  - --forwarding-domains=projecta.svc.cluster.local
  - --forwarding-domains=projectb.svc.cluster.local
  - --tls-ca-certificate=/etc/secrets/ca.pem
  - --tls-ca-key=/etc/secrets/ca-key.pem
  # Note: if you don't specify any forwarding domains, all domains will be signed; Also the code checks is the {#}
  # domain 'contains' the value (it's not a regex) so if you wanted to sign all requests to svc.cluster.local, just use {#}
  # svc.cluster.local {#}
  volumeMounts:
  - name: keycloak-socket
    mountPoint: /var/run/keycloak
- name: projecta
  image: some_images

# test the forward proxy {#}
$ curl -k --proxy http://127.0.0.1:3000 https://test.projesta.svc.cluster.local
```

On the receiver side you could set up the Keycloak Gatekeeper (--no=redirects=true) and permit this to verify and handle admission for you. Alternatively, the access token can found as a bearer token in the request.

#### 2.4.8. Forwarding signed HTTPS connections {#}

Handling HTTPS requires a man-in-the-middle sort of TLS connection. By default, if no `--tls-ca-certificate` and `--tls-ca-key` are provided the proxy will use the default certificate. If you wish to verify the trust, you’ll need to generate a CA, for example.

```
$ openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout ca.key -out ca.pem
$ bin/keycloak-gatekeeper \
  --enable-forwarding \
  --forwarding-username=USERNAME \
  --forwarding-password=PASSWORD \
  --client-id=CLIENT_ID \
  --client-secret=SECRET \
  --discovery-url=https://keycloak.example.com/auth/realms/test \
  --tls-ca-certificate=ca.pem \
  --tls-ca-key=ca-key.pem
```

#### 2.4.9. HTTPS redirect {#}

The proxy supports an HTTP listener, so the only real requirement here is to perform an HTTP → HTTPS redirect. You can enable the option like this:

```
--listen-http=127.0.0.1:80
--enable-security-filter=true  # is required for the https redirect
--enable-https-redirection
```

#### 2.4.10. Let’s Encrypt configuration {#}

Here is an example of the required configuration for Let’s Encrypt support:

```
listen: 0.0.0.0:443
enable-https-redirection: true
enable-security-filter: true
use-letsencrypt: true
letsencrypt-cache-dir: ./cache/
redirection-url: https://domain.tld:443/
hostnames:
  - domain.tld
```

Listening on port 443 is mandatory.

#### 2.4.11. Access token encryption {#}

By default, the session token is placed into a cookie in plaintext. If you prefer to encrypt the session cookie, use the `--enable-encrypted-token` and `--encryption-key` options. Note that the access token forwarded in the X-Auth-Token header to upstream is unaffected.

#### 2.4.12. Upstream headers {#}

On protected resources, the upstream endpoint will receive a number of headers added by the proxy, along with custom claims, like this:

```
# add the header to the upstream endpoint {#}
id := user.(*userContext)
cx.Request().Header.Set("X-Auth-Email", id.email)
cx.Request().Header.Set("X-Auth-ExpiresIn", id.expiresAt.String())
cx.Request().Header.Set("X-Auth-Groups", strings.Join(id.groups, ","))
cx.Request().Header.Set("X-Auth-Roles", strings.Join(id.roles, ","))
cx.Request().Header.Set("X-Auth-Subject", id.id)
cx.Request().Header.Set("X-Auth-Token", id.token.Encode())
cx.Request().Header.Set("X-Auth-Userid", id.name)
cx.Request().Header.Set("X-Auth-Username", id.name)
// step: add the authorization header if requested
if r.config.EnableAuthorizationHeader {
        cx.Request().Header.Set("Authorization", fmt.Sprintf("Bearer %s", id.token.Encode()))
}
```

To control the `Authorization` header use the `enable-authorization-header` yaml configuration or the `--enable-authorization-header` command line option. By default this option is set to `true`.

#### 2.4.13. Custom claim headers {#}

You can inject additional claims from the access token into the authorization headers with the `--add-claims` option. For example, a token from a Keycloak provider might include the following claims:

```
"resource_access": {},
"name": "Beloved User",
"preferred_username": "beloved.user",
"given_name": "Beloved",
"family_name": "User",
"email": "beloved@example.com"
```

In order to request you receive the given_name, family_name and name in the authentication header we would add `--add-claims=given_name` and `--add-claims=family_name` and so on, or we can do it in the configuration file, like this:

```
add-claims:
- given_name
- family_name
- name
```

This would add the additional headers to the authenticated request along with standard ones.

```
X-Auth-Family-Name: User
X-Auth-Given-Name: Beloved
X-Auth-Name: Beloved User
```

#### 2.4.14. Custom headers {#}

You can inject custom headers using the `--headers="name=value"` option or the configuration file:

```
headers:
  name: value
```

#### 2.4.15. Encryption key {#}

In order to remain stateless and not have to rely on a central cache to persist the refresh_tokens, the refresh token is encrypted and added as a cookie using **crypto/aes**. The key must be the same if you are running behind a load balancer. The key length should be either 16 or 32 bytes, depending or whether you want AES-128 or AES-256.

#### 2.4.16. Claim matching {#}

The proxy supports adding a variable list of claim matches against the presented tokens for additional access control. You can match the 'iss' or 'aud' to the token or custom attributes; each of the matches are regex’s. For example, `--match-claims 'aud=sso.*'` or `--claim iss=https://.*'` or via the configuration file, like this:

```
match-claims:
  aud: openvpn
  iss: https://keycloak.example.com/auth/realms/commons
```

or via the CLI, like this:

```
--match-claims=auth=openvpn
--match-claims=iss=http://keycloak.example.com/realms/commons
```

You can limit the email domain permitted; for example if you want to limit to only users on the example.com domain:

```
match-claims:
  email: ^.*@example.com$
```

The adapter supports matching on multi-value strings claims. The match will succeed if one of the values matches, for example:

```
match-claims:
  perms: perm1
```

will successfully match

```
{
  "iss": "https://sso.example.com",
  "sub": "",
  "perms": ["perm1", "perm2"]
}
```

#### 2.4.17. Group claims {#}

You can match on the group claims within a token via the `groups` parameter available within the resource. While roles are implicitly required, such as `roles=admin,user` where the user MUST have roles 'admin' AND 'user', groups are applied with an OR operation, so `groups=users,testers` requires that the user MUST be within either 'users' OR 'testers'. The claim name is hard-coded to `groups`, so a JWT token would look like this:

```
{
  "iss": "https://sso.example.com",
  "sub": "",
  "aud": "test",
  "exp": 1515269245,
  "iat": 1515182845,
  "email": "beloved@example.com",
  "groups": [
    "group_one",
    "group_two"
  ],
  "name": "Beloved"
}
```

#### 2.4.18. Custom pages {#}

By default, Keycloak Gatekeeper will immediately redirect you for authentication and hand back a 403 for access denied. Most users will probably want to present the user with a more friendly sign-in and access denied page. You can pass the command line options (or via config file) paths to the files with `--signin-page=PATH`. The sign-in page will have a 'redirect' variable passed into the scope and holding the oauth redirection url. If you wish to pass additional variables into the templates, such as title, sitename and so on, you can use the -`-tags key=pair` option, like this: `--tags title="This is my site"` and the variable would be accessible from `{{ .title }}`.

```
<html>
<body>
<a href="{{ .redirect }}">Sign-in</a>
</body>
</html>
```

#### 2.4.19. White-listed URL’s {#}

Depending on how the application URL’s are laid out, you might want protect the root / url but have exceptions on a list of paths, for example `/health`. While this is best solved by adjusting the paths, you can add exceptions to the protected resources, like this:

```
  resources:
  - uri: /some_white_listed_url
    white-listed: true
  - uri: /*
    methods:
      - GET
    roles:
      - <CLIENT_APP_NAME>:<ROLE_NAME>
      - <CLIENT_APP_NAME>:<ROLE_NAME>
```

Or on the command line

```
  --resources "uri=/some_white_listed_url|white-listed=true"
  --resources "uri=/*"  # requires authentication on the rest
  --resources "uri=/admin*|roles=admin,superuser|methods=POST,DELETE"
```

#### 2.4.20. Mutual TLS {#}

The proxy support enforcing mutual TLS for the clients by adding the `--tls-ca-certificate` command line option or configuration file option. All clients connecting must present a certificate which was signed by the CA being used.

#### 2.4.21. Certificate rotation {#}

The proxy will automatically rotate the server certificates if the files change on disk. Note, no down time will occur as the change is made inline. Clients who connected prior to the certificate rotation will be unaffected and will continue as normal with all new connections presented with the new certificate.

#### 2.4.22. Refresh tokens {#}

If a request for an access token contains a refresh token and `--enable-refresh-tokens` is set to `true`, the proxy will automatically refresh the access token for you. The tokens themselves are kept either as an encrypted **(--encryption-key=KEY)** cookie **(cookie name: kc-state).** or a store **(still requires encryption key)**.

At present the only store options supported are [Redis](https://github.com/antirez/redis) and [Boltdb](https://github.com/boltdb/bolt).

To enable a local boltdb store use `--store-url boltdb:///PATH` or using a relative path `boltdb://PATH`.

To enable a local redis store use `redis://[USER:PASSWORD@]HOST:PORT`. In both cases the refresh token is encrypted before being placed into the store.

#### 2.4.23. Logout endpoint {#}

A **/oauth/logout?redirect=url** is provided as a helper to log users out. In addition to dropping any session cookies, we also attempt to revoke access via revocation url (config **revocation-url** or **--revocation-url**) with the provider. For Keycloak, the url for this would be <https://keycloak.example.com/auth/realms/REALM_NAME/protocol/openid-connect/logout>. If the url is not specified we will attempt to grab the url from the OpenID discovery response.

#### 2.4.24. Cross-origin resource sharing (CORS) {#}

You can add a CORS header via the `--cors-[method]` with these configuration options.

- Access-Control-Allow-Origin
- Access-Control-Allow-Methods
- Access-Control-Allow-Headers
- Access-Control-Expose-Headers
- Access-Control-Allow-Credentials
- Access-Control-Max-Age

You can add using the config file:

```
cors-origins:
- '*'
cors-methods:
- GET
- POST
```

or via the command line:

```
--cors-origins [--cors-origins option]                  a set of origins to add to the CORS access control (Access-Control-Allow-Origin)
--cors-methods [--cors-methods option]                  the method permitted in the access control (Access-Control-Allow-Methods)
--cors-headers [--cors-headers option]                  a set of headers to add to the CORS access control (Access-Control-Allow-Headers)
--cors-exposes-headers [--cors-exposes-headers option]  set the expose cors headers access control (Access-Control-Expose-Headers)
```

#### 2.4.25. Upstream URL {#}

You can control the upstream endpoint via the `--upstream-url` option. Both HTTP and HTTPS are supported with TLS verification and keep-alive support configured via the `--skip-upstream-tls-verify` / `--upstream-keepalives` option. Note, the proxy can also upstream via a UNIX socket, `--upstream-url unix://path/to/the/file.sock`.

#### 2.4.26. Endpoints {#}

- **/oauth/authorize** is authentication endpoint which will generate the OpenID redirect to the provider
- **/oauth/callback** is provider OpenID callback endpoint
- **/oauth/expired** is a helper endpoint to check if a access token has expired, 200 for ok and, 401 for no token and 401 for expired
- **/oauth/health** is the health checking endpoint for the proxy, you can also grab version from headers
- **/oauth/login** provides a relay endpoint to login via `grant_type=password`, for example, `POST /oauth/login` form values are `username=USERNAME&password=PASSWORD` (must be enabled)
- **/oauth/logout** provides a convenient endpoint to log the user out, it will always attempt to perform a back channel log out of offline tokens
- **/oauth/token** is a helper endpoint which will display the current access token for you
- **/oauth/metrics** is a Prometheus metrics handler

#### 2.4.27. Metrics {#}

Assuming `--enable-metrics` has been set, a Prometheus endpoint can be found on **/oauth/metrics**; at present the only metric being exposed is a counter per HTTP code.

#### 2.4.28. Limitations {#}

Keep in mind [browser cookie limits](http://browsercookielimits.squawky.net/) if you use access or refresh tokens in the browser cookie. Keycloak-generic-adapter divides the cookie automatically if your cookie is longer than 4093 bytes. Real size of the cookie depends on the content of the issued access token. Also, encryption might add additional bytes to the cookie size. If you have large cookies (>200 KB), you might reach browser cookie limits.

All cookies are part of the header request, so you might find a problem with the max headers size limits in your infrastructure (some load balancers have very low this value, such as 8 KB). Be sure that all network devices have sufficient header size limits. Otherwise, your users won’t be able to obtain an access token.

#### 2.4.29. Known Issues {#}

- There is a known issue with the Keycloak server 4.7.0.Final in which Gatekeeper is unable to find the *client_id* in the *aud*claim. This is due to the fact the *client_id* is not in the audience anymore. The workaround is to add the "Audience" protocol mapper to the client with the audience pointed to the *client_id*. For more information, see [KEYCLOAK-8954](https://issues.jboss.org/browse/KEYCLOAK-8954). ==== mod_auth_openidc Apache HTTPD Module

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/mod-auth-openidc.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/mod-auth-openidc.adoc)

The [mod_auth_openidc](https://github.com/zmartzone/mod_auth_openidc) is an Apache HTTP plugin for OpenID Connect. If your language/environment supports using Apache HTTPD as a proxy, then you can use *mod_auth_openidc* to secure your web application with OpenID Connect. Configuration of this module is beyond the scope of this document. Please see the *mod_auth_openidc* GitHub repo for more details on configuration.

To configure *mod_auth_openidc* you’ll need

- The client_id.
- The client_secret.
- The redirect_uri to your application.
- The Keycloak openid-configuration url
- *mod_auth_openidc* specific Apache HTTPD module config.

An example configuration would look like the following.

```
LoadModule auth_openidc_module modules/mod_auth_openidc.so

ServerName ${HOSTIP}

<VirtualHost *:80>

    ServerAdmin webmaster@localhost
    DocumentRoot /var/www/html

    #this is required by mod_auth_openidc
    OIDCCryptoPassphrase a-random-secret-used-by-apache-oidc-and-balancer

    OIDCProviderMetadataURL ${KC_ADDR}/auth/realms/${KC_REALM}/.well-known/openid-configuration

    OIDCClientID ${CLIENT_ID}
    OIDCClientSecret ${CLIENT_SECRET}
    OIDCRedirectURI http://${HOSTIP}/${CLIENT_APP_NAME}/redirect_uri

    # maps the prefered_username claim to the REMOTE_USER environment variable {#}
    OIDCRemoteUserClaim preferred_username

    <Location /${CLIENT_APP_NAME}/>
        AuthType openid-connect
        Require valid-user
    </Location>
</VirtualHost>
```

Further information on how to configure mod_auth_openidc can be found on the [mod_auth_openidc](https://github.com/zmartzone/mod_auth_openidc) project page.

### 2.5. Other OpenID Connect Libraries {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/oidc-generic.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/oidc-generic.adoc)

Keycloak can be secured by supplied adapters that are usually easier to use and provide better integration with Keycloak. However, if an adapter is not available for your programming language, framework, or platform you might opt to use a generic OpenID Connect Resource Provider (RP) library instead. This chapter describes details specific to Keycloak and does not contain specific protocol details. For more information see the [OpenID Connect specifications](https://openid.net/connect/) and [OAuth2 specification](https://tools.ietf.org/html/rfc6749).

#### 2.5.1. Endpoints {#}

The most important endpoint to understand is the `well-known` configuration endpoint. It lists endpoints and other configuration options relevant to the OpenID Connect implementation in Keycloak. The endpoint is:

```
/realms/{realm-name}/.well-known/openid-configuration
```

To obtain the full URL, add the base URL for Keycloak and replace `{realm-name}` with the name of your realm. For example:

http://localhost:8080/auth/realms/master/.well-known/openid-configuration

Some RP libraries retrieve all required endpoints from this endpoint, but for others you might need to list the endpoints individually.

##### Authorization Endpoint {#}

```
/realms/{realm-name}/protocol/openid-connect/auth
```

The authorization endpoint performs authentication of the end-user. This is done by redirecting the user agent to this endpoint.

For more details see the [Authorization Endpoint](https://openid.net/specs/openid-connect-core-1_0.html#AuthorizationEndpoint) section in the OpenID Connect specification.

##### Token Endpoint {#}

```
/realms/{realm-name}/protocol/openid-connect/token
```

The token endpoint is used to obtain tokens. Tokens can either be obtained by exchanging an authorization code or by supplying credentials directly depending on what flow is used. The token endpoint is also used to obtain new access tokens when they expire.

For more details see the [Token Endpoint](https://openid.net/specs/openid-connect-core-1_0.html#TokenEndpoint) section in the OpenID Connect specification.

##### Userinfo Endpoint {#}

```
/realms/{realm-name}/protocol/openid-connect/userinfo
```

The userinfo endpoint returns standard claims about the authenticated user, and is protected by a bearer token.

For more details see the [Userinfo Endpoint](https://openid.net/specs/openid-connect-core-1_0.html#UserInfo) section in the OpenID Connect specification.

##### Logout Endpoint {#}

```
/realms/{realm-name}/protocol/openid-connect/logout
```

The logout endpoint logs out the authenticated user.

The user agent can be redirected to the endpoint, in which case the active user session is logged out. Afterward the user agent is redirected back to the application.

The endpoint can also be invoked directly by the application. To invoke this endpoint directly the refresh token needs to be included as well as the credentials required to authenticate the client.

##### Certificate Endpoint {#}

```
/realms/{realm-name}/protocol/openid-connect/certs
```

The certificate endpoint returns the public keys enabled by the realm, encoded as a JSON Web Key (JWK). Depending on the realm settings there can be one or more keys enabled for verifying tokens. For more information see the [Server Administration Guide](https://www.keycloak.org/docs/6.0/server_admin/) and the [JSON Web Key specification](https://tools.ietf.org/html/rfc7517).

##### Introspection Endpoint {#}

```
/realms/{realm-name}/protocol/openid-connect/token/introspect
```

The introspection endpoint is used to retrieve the active state of a token. In other words, you can use it to validate an access or refresh token. It can only be invoked by confidential clients.

For more details on how to invoke on this endpoint, see [OAuth 2.0 Token Introspection specification](https://tools.ietf.org/html/rfc7662).

##### Dynamic Client Registration Endpoint {#}

```
/realms/{realm-name}/clients-registrations/openid-connect
```

The dynamic client registration endpoint is used to dynamically register clients.

For more details see the [Client Registration chapter](https://www.keycloak.org/docs/latest/securing_apps/index.html#_client_registration) and the [OpenID Connect Dynamic Client Registration specification](https://openid.net/specs/openid-connect-registration-1_0.html).

#### 2.5.2. Validating Access Tokens {#}

If you need to manually validate access tokens issued by Keycloak you can invoke the [Introspection Endpoint](https://www.keycloak.org/docs/latest/securing_apps/index.html#_token_introspection_endpoint). The downside to this approach is that you have to make a network invocation to the Keycloak server. This can be slow and possibily overload the server if you have too many validation requests going on at the same time. Keycloak issued access tokens are [JSON Web Tokens (JWT)](https://tools.ietf.org/html/rfc7519) digitally signed and encoded using [JSON Web Signature (JWS)](https://www.rfc-editor.org/rfc/rfc7515.txt). Because they are encoded in this way, this allows you to locally validate access tokens using the public key of the issuing realm. You can either hard code the realm’s public key in your validation code, or lookup and cache the public key using the [certificate endpoint](https://www.keycloak.org/docs/latest/securing_apps/index.html#_certificate_endpoint) with the Key ID (KID) embedded within the JWS. Depending what language you code in, there are a multitude of third party libraries out there that can help you with JWS validation.

#### 2.5.3. Flows {#}

##### Authorization Code {#}

The Authorization Code flow redirects the user agent to Keycloak. Once the user has successfully authenticated with Keycloak an Authorization Code is created and the user agent is redirected back to the application. The application then uses the authorization code along with its credentials to obtain an Access Token, Refresh Token and ID Token from Keycloak.

The flow is targeted towards web applications, but is also recommended for native applications, including mobile applications, where it is possible to embed a user agent.

For more details refer to the [Authorization Code Flow](https://openid.net/specs/openid-connect-core-1_0.html#CodeFlowAuth) in the OpenID Connect specification.

##### Implicit {#}

The Implicit flow redirects works similarly to the Authorization Code flow, but instead of returning an Authorization Code the Access Token and ID Token is returned. This reduces the need for the extra invocation to exchange the Authorization Code for an Access Token. However, it does not include a Refresh Token. This results in the need to either permit Access Tokens with a long expiration, which is problematic as it’s very hard to invalidate these. Or requires a new redirect to obtain new Access Token once the initial Access Token has expired. The Implicit flow is useful if the application only wants to authenticate the user and deals with logout itself.

There’s also a Hybrid flow where both the Access Token and an Authorization Code is returned.

One thing to note is that both the Implicit flow and Hybrid flow has potential security risks as the Access Token may be leaked through web server logs and browser history. This is somewhat mitigated by using short expiration for Access Tokens.

For more details refer to the [Implicit Flow](https://openid.net/specs/openid-connect-core-1_0.html#ImplicitFlowAuth) in the OpenID Connect specification.

##### Resource Owner Password Credentials {#}

Resource Owner Password Credentials, referred to as Direct Grant in Keycloak, allows exchanging user credentials for tokens. It’s not recommended to use this flow unless you absolutely need to. Examples where this could be useful are legacy applications and command-line interfaces.

There are a number of limitations of using this flow, including:

- User credentials are exposed to the application
- Applications need login pages
- Application needs to be aware of the authentication scheme
- Changes to authentication flow requires changes to application
- No support for identity brokering or social login
- Flows are not supported (user self-registration, required actions, etc.)

For a client to be permitted to use the Resource Owner Password Credentials grant the client has to have the `Direct Access Grants Enabled` option enabled.

This flow is not included in OpenID Connect, but is a part of the OAuth 2.0 specification.

For more details refer to the [Resource Owner Password Credentials Grant](https://tools.ietf.org/html/rfc6749#section-4.3) chapter in the OAuth 2.0 specification.

###### Example using CURL {#}

The following example shows how to obtain an access token for a user in the realm `master` with username `user` and password `password`. The example is using the confidential client `myclient`:

```
curl \
  -d "client_id=myclient" \
  -d "client_secret=40cc097b-2a57-4c17-b36a-8fdf3fc2d578" \
  -d "username=user" \
  -d "password=password" \
  -d "grant_type=password" \
  "http://localhost:8080/auth/realms/master/protocol/openid-connect/token"
```

##### Client Credentials {#}

Client Credentials is used when clients (applications and services) wants to obtain access on behalf of themselves rather than on behalf of a user. This can for example be useful for background services that applies changes to the system in general rather than for a specific user.

Keycloak provides support for clients to authenticate either with a secret or with public/private keys.

This flow is not included in OpenID Connect, but is a part of the OAuth 2.0 specification.

For more details refer to the [Client Credentials Grant](https://tools.ietf.org/html/rfc6749#section-4.4) chapter in the OAuth 2.0 specification.

#### 2.5.4. Redirect URIs {#}

When using the redirect based flows it’s important to use valid redirect uris for your clients. The redirect uris should be as specific as possible. This especially applies to client-side (public clients) applications. Failing to do so could result in:

- Open redirects - this can allow attackers to create spoof links that looks like they are coming from your domain
- Unauthorized entry - when users are already authenticated with Keycloak an attacker can use a public client where redirect uris have not be configured correctly to gain access by redirecting the user without the users knowledge

In production for web applications always use `https` for all redirect URIs. Do not allow redirects to http.

There’s also a few special redirect URIs:

- `http://localhost`

  This redirect URI is useful for native applications and allows the native application to create a web server on a random port that can be used to obtain the authorization code. This redirect uri allows any port.

- `urn:ietf:wg:oauth:2.0:oob`

  If its not possible to start a web server in the client (or a browser is not available) it is possible to use the special `urn:ietf:wg:oauth:2.0:oob` redirect uri. When this redirect uri is used Keycloak displays a page with the code in the title and in a box on the page. The application can either detect that the browser title has changed, or the user can copy/paste the code manually to the application. With this redirect uri it is also possible for a user to use a different device to obtain a code to paste back to the application.

## 3. SAML {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/saml-overview.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/saml-overview.adoc)

This section describes how you can secure applications and services with SAML using either Keycloak client adapters or generic SAML provider libraries.

### 3.1. Java Adapters {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/java-adapters.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/java-adapters.adoc)

Keycloak comes with a range of different adapters for Java application. Selecting the correct adapter depends on the target platform.

#### 3.1.1. General Adapter Config {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/general-config.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/general-config.adoc)

Each SAML client adapter supported by Keycloak can be configured by a simple XML text file. This is what one might look like:

```
<keycloak-saml-adapter xmlns="urn:keycloak:saml:adapter"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                       xsi:schemaLocation="urn:keycloak:saml:adapter https://www.keycloak.org/schema/keycloak_saml_adapter_1_10.xsd">
    <SP entityID="http://localhost:8081/sales-post-sig/"
        sslPolicy="EXTERNAL"
        nameIDPolicyFormat="urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified"
        logoutPage="/logout.jsp"
        forceAuthentication="false"
        isPassive="false"
        turnOffChangeSessionIdOnLogin="false"
        autodetectBearerOnly="false">
        <Keys>
            <Key signing="true" >
                <KeyStore resource="/WEB-INF/keystore.jks" password="store123">
                    <PrivateKey alias="http://localhost:8080/sales-post-sig/" password="test123"/>
                    <Certificate alias="http://localhost:8080/sales-post-sig/"/>
                </KeyStore>
            </Key>
        </Keys>
        <PrincipalNameMapping policy="FROM_NAME_ID"/>
        <RoleIdentifiers>
            <Attribute name="Role"/>
        </RoleIdentifiers>
        <IDP entityID="idp"
             signaturesRequired="true">
        <SingleSignOnService requestBinding="POST"
                             bindingUrl="http://localhost:8081/auth/realms/demo/protocol/saml"
                    />

            <SingleLogoutService
                    requestBinding="POST"
                    responseBinding="POST"
                    postBindingUrl="http://localhost:8081/auth/realms/demo/protocol/saml"
                    redirectBindingUrl="http://localhost:8081/auth/realms/demo/protocol/saml"
                    />
            <Keys>
                <Key signing="true">
                    <KeyStore resource="/WEB-INF/keystore.jks" password="store123">
                        <Certificate alias="demo"/>
                    </KeyStore>
                </Key>
            </Keys>
        </IDP>
     </SP>
</keycloak-saml-adapter>
```

Some of these configuration switches may be adapter specific and some are common across all adapters. For Java adapters you can use `${…}` enclosure as System property replacement. For example `${jboss.server.config.dir}`.

##### SP Element {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/general-config/sp_element.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/general-config/sp_element.adoc)

Here is the explanation of the SP element attributes:

```
<SP entityID="sp"
    sslPolicy="ssl"
    nameIDPolicyFormat="format"
    forceAuthentication="true"
    isPassive="false"
    autodetectBearerOnly="false">
...
</SP>
```

- entityID

  This is the identifier for this client. The IdP needs this value to determine who the client is that is communicating with it. This setting is *REQUIRED*.

- sslPolicy

  This is the SSL policy the adapter will enforce. Valid values are: `ALL`, `EXTERNAL`, and `NONE`. For `ALL`, all requests must come in via HTTPS. For `EXTERNAL`, only non-private IP addresses must come over the wire via HTTPS. For `NONE`, no requests are required to come over via HTTPS. This setting is *OPTIONAL*. Default value is `EXTERNAL`.

- nameIDPolicyFormat

  SAML clients can request a specific NameID Subject format. Fill in this value if you want a specific format. It must be a standard SAML format identifier: `urn:oasis:names:tc:SAML:2.0:nameid-format:transient`. This setting is *OPTIONAL*. By default, no special format is requested.

- forceAuthentication

  SAML clients can request that a user is re-authenticated even if they are already logged in at the IdP. Set this to `true` to enable. This setting is *OPTIONAL*. Default value is `false`.

- isPassive

  SAML clients can request that a user is never asked to authenticate even if they are not logged in at the IdP. Set this to `true` if you want this. Do not use together with `forceAuthentication` as they are opposite. This setting is *OPTIONAL*. Default value is `false`.

- turnOffChangeSessionIdOnLogin

  The session ID is changed by default on a successful login on some platforms to plug a security attack vector. Change this to `true` to disable this. It is recommended you do not turn it off. Default value is `false`.

- autodetectBearerOnly

  This should be set to *true* if your application serves both a web application and web services (e.g. SOAP or REST). It allows you to redirect unauthenticated users of the web application to the Keycloak login page, but send an HTTP `401` status code to unauthenticated SOAP or REST clients instead as they would not understand a redirect to the login page. Keycloak auto-detects SOAP or REST clients based on typical headers like `X-Requested-With`, `SOAPAction` or `Accept`. The default value is *false*.

- logoutPage

  This sets the page to display after logout. If the page is a full URL, such as `http://web.example.com/logout.html`, the user is redirected after logout to that page using the HTTP `302` status code. If a link without scheme part is specified, such as `/logout.jsp`, the page is displayed after logout, *regardless of whether it lies in a protected area according to security-constraint declarations in web.xml*, and the page is resolved relative to the deployment context root.

##### Service Provider Keys and Key Elements {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/general-config/sp-keys.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/general-config/sp-keys.adoc)

If the IdP requires that the client application (or SP) sign all of its requests and/or if the IdP will encrypt assertions, you must define the keys used to do this. For client-signed documents you must define both the private and public key or certificate that is used to sign documents. For encryption, you only have to define the private key that is used to decrypt it.

There are two ways to describe your keys. They can be stored within a Java KeyStore or you can copy/paste the keys directly within `keycloak-saml.xml` in the PEM format.

```
        <Keys>
            <Key signing="true" >
               ...
            </Key>
        </Keys>
```

The `Key` element has two optional attributes `signing` and `encryption`. When set to true these tell the adapter what the key will be used for. If both attributes are set to true, then the key will be used for both signing documents and decrypting encrypted assertions. You must set at least one of these attributes to true.

###### KeyStore element {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/general-config/sp-keys/keystore_element.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/general-config/sp-keys/keystore_element.adoc)

Within the `Key` element you can load your keys and certificates from a Java Keystore. This is declared within a `KeyStore` element.

```
        <Keys>
            <Key signing="true" >
                <KeyStore resource="/WEB-INF/keystore.jks" password="store123">
                    <PrivateKey alias="myPrivate" password="test123"/>
                    <Certificate alias="myCertAlias"/>
                </KeyStore>
            </Key>
        </Keys>
```

Here are the XML config attributes that are defined with the `KeyStore` element.

- file

  File path to the key store. This option is *OPTIONAL*. The file or resource attribute must be set.

- resource

  WAR resource path to the KeyStore. This is a path used in method call to ServletContext.getResourceAsStream(). This option is *OPTIONAL*. The file or resource attribute must be set.

- password

  The password of the KeyStore. This option is *REQUIRED*.

If you are defining keys that the SP will use to sign document, you must also specify references to your private keys and certificates within the Java KeyStore. The `PrivateKey` and `Certificate` elements in the above example define an `alias`that points to the key or cert within the keystore. Keystores require an additional password to access private keys. In the `PrivateKey` element you must define this password within a `password` attribute.

###### Key PEMS {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/general-config/sp-keys/key_pems.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/general-config/sp-keys/key_pems.adoc)

Within the `Key` element you declare your keys and certificates directly using the sub elements`PrivateKeyPem`, `PublicKeyPem`, and `CertificatePem`. The values contained in these elements must conform to the PEM key format. You usually use this option if you are generating keys using `openssl` or similar command line tool.

```
<Keys>
   <Key signing="true">
      <PrivateKeyPem>
         2341251234AB31234==231BB998311222423522334
      </PrivateKeyPem>
      <CertificatePem>
         211111341251234AB31234==231BB998311222423522334
      </CertificatePem>
   </Key>
</Keys>
```

##### SP PrincipalNameMapping element {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/general-config/sp_principalname_mapping_element.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/general-config/sp_principalname_mapping_element.adoc)

This element is optional. When creating a Java Principal object that you obtain from methods such as `HttpServletRequest.getUserPrincipal()`, you can define what name is returned by the `Principal.getName()` method.

```
<SP ...>
  <PrincipalNameMapping policy="FROM_NAME_ID"/>
</SP>

<SP ...>
  <PrincipalNameMapping policy="FROM_ATTRIBUTE" attribute="email" />
</SP>
```

The `policy` attribute defines the policy used to populate this value. The possible values for this attribute are:

- FROM_NAME_ID

  This policy just uses whatever the SAML subject value is. This is the default setting

- FROM_ATTRIBUTE

  This will pull the value from one of the attributes declared in the SAML assertion received from the server. You’ll need to specify the name of the SAML assertion attribute to use within the `attribute` XML attribute.

##### RoleIdentifiers Element {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/general-config/roleidentifiers_element.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/general-config/roleidentifiers_element.adoc)

The `RoleIdentifiers` element defines what SAML attributes within the assertion received from the user should be used as role identifiers within the Java EE Security Context for the user.

```
<RoleIdentifiers>
     <Attribute name="Role"/>
     <Attribute name="member"/>
     <Attribute name="memberOf"/>
</RoleIdentifiers>
```

By default `Role` attribute values are converted to Java EE roles. Some IdPs send roles using a `member` or `memberOf`attribute assertion. You can define one or more `Attribute` elements to specify which SAML attributes must be converted into roles.

##### IDP Element {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/general-config/idp_element.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/general-config/idp_element.adoc)

Everything in the IDP element describes the settings for the identity provider (authentication server) the SP is communicating with.

```
<IDP entityID="idp"
     signaturesRequired="true"
     signatureAlgorithm="RSA_SHA1"
     signatureCanonicalizationMethod="http://www.w3.org/2001/10/xml-exc-c14n#">
...
</IDP>
```

Here are the attribute config options you can specify within the `IDP` element declaration.

- entityID

  This is the issuer ID of the IDP. This setting is *REQUIRED*.

- signaturesRequired

  If set to `true`, the client adapter will sign every document it sends to the IDP. Also, the client will expect that the IDP will be signing any documents sent to it. This switch sets the default for all request and response types, but you will see later that you have some fine grain control over this. This setting is *OPTIONAL* and will default to `false`.

- signatureAlgorithm

  This is the signature algorithm that the IDP expects signed documents to use. Allowed values are: `RSA_SHA1`, `RSA_SHA256`, `RSA_SHA512`, and `DSA_SHA1`. This setting is *OPTIONAL* and defaults to `RSA_SHA256`.

- signatureCanonicalizationMethod

  This is the signature canonicalization method that the IDP expects signed documents to use. This setting is *OPTIONAL*. The default value is `http://www.w3.org/2001/10/xml-exc-c14n#` and should be good for most IDPs.

- metadataUrl

  The URL used to retrieve the IDP metadata, currently this is only used to pick up signing and encryption keys periodically which allow cycling of these keys on the IDP without manual changes on the SP side.

##### IDP SingleSignOnService sub element {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/general-config/idp_singlesignonservice_subelement.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/general-config/idp_singlesignonservice_subelement.adoc)

The `SingleSignOnService` sub element defines the login SAML endpoint of the IDP. The client adapter will send requests to the IDP formatted via the settings within this element when it wants to login.

```
<SingleSignOnService signRequest="true"
                     validateResponseSignature="true"
                     requestBinding="post"
                     bindingUrl="url"/>
```

Here are the config attributes you can define on this element:

- signRequest

  Should the client sign authn requests? This setting is *OPTIONAL*. Defaults to whatever the IDP `signaturesRequired`element value is.

- validateResponseSignature

  Should the client expect the IDP to sign the assertion response document sent back from an auhtn request? This setting *OPTIONAL*. Defaults to whatever the IDP `signaturesRequired` element value is.

- requestBinding

  This is the SAML binding type used for communicating with the IDP. This setting is *OPTIONAL*. The default value is `POST`, but you can set it to `REDIRECT` as well.

- responseBinding

  SAML allows the client to request what binding type it wants authn responses to use. The values of this can be `POST` or `REDIRECT`. This setting is *OPTIONAL*. The default is that the client will not request a specific binding type for responses.

- assertionConsumerServiceUrl

  URL of the assertion consumer service (ACS) where the IDP login service should send responses to. This setting is *OPTIONAL*. By default it is unset, relying on the configuration in the IdP. When set, it must end in `/saml`, e.g. `http://sp.domain.com/my/endpoint/for/saml`. The value of this property is sent in `AssertionConsumerServiceURL`attribute of SAML `AuthnRequest` message. This property is typically accompanied by the `responseBinding` attribute.

- bindingUrl

  This is the URL for the IDP login service that the client will send requests to. This setting is *REQUIRED*.

##### IDP SingleLogoutService sub element {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/general-config/idp_singlelogoutservice_subelement.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/general-config/idp_singlelogoutservice_subelement.adoc)

The `SingleLogoutService` sub element defines the logout SAML endpoint of the IDP. The client adapter will send requests to the IDP formatted via the settings within this element when it wants to logout.

```
<SingleLogoutService validateRequestSignature="true"
                     validateResponseSignature="true"
                     signRequest="true"
                     signResponse="true"
                     requestBinding="redirect"
                     responseBinding="post"
                     postBindingUrl="posturl"
                     redirectBindingUrl="redirecturl">
```

- signRequest

  Should the client sign logout requests it makes to the IDP? This setting is *OPTIONAL*. Defaults to whatever the IDP `signaturesRequired` element value is.

- signResponse

  Should the client sign logout responses it sends to the IDP requests? This setting is *OPTIONAL*. Defaults to whatever the IDP `signaturesRequired` element value is.

- validateRequestSignature

  Should the client expect signed logout request documents from the IDP? This setting is *OPTIONAL*. Defaults to whatever the IDP `signaturesRequired` element value is.

- validateResponseSignature

  Should the client expect signed logout response documents from the IDP? This setting is *OPTIONAL*. Defaults to whatever the IDP `signaturesRequired` element value is.

- requestBinding

  This is the SAML binding type used for communicating SAML requests to the IDP. This setting is *OPTIONAL*. The default value is `POST`, but you can set it to REDIRECT as well.

- responseBinding

  This is the SAML binding type used for communicating SAML responses to the IDP. The values of this can be `POST` or `REDIRECT`. This setting is *OPTIONAL*. The default value is `POST`, but you can set it to `REDIRECT` as well.

- postBindingUrl

  This is the URL for the IDP’s logout service when using the POST binding. This setting is *REQUIRED* if using the `POST`binding.

- redirectBindingUrl

  This is the URL for the IDP’s logout service when using the REDIRECT binding. This setting is *REQUIRED* if using the REDIRECT binding.

##### IDP Keys sub element {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/general-config/idp_keys_subelement.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/general-config/idp_keys_subelement.adoc)

The Keys sub element of IDP is only used to define the certificate or public key to use to verify documents signed by the IDP. It is defined in the same way as the [SP’s Keys element](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml-sp-keys). But again, you only have to define one certificate or public key reference. Note that, if both IDP and SP are realized by Keycloak server and adapter, respectively, there is no need to specify the keys for signature validation, see below.

It is possible to configure SP to obtain public keys for IDP signature validation from published certificates automatically, provided both SP and IDP are implemented by Keycloak. This is done by removing all declarations of signature validation keys in Keys sub element. If the Keys sub element would then remain empty, it can be omitted completely. The keys are then automatically obtained by SP from SAML descriptor, location of which is derived from SAML endpoint URL specified in the [IDP SingleSignOnService sub element](https://www.keycloak.org/docs/latest/securing_apps/index.html#_sp-idp-singlesignonservice). Settings of the HTTP client that is used for SAML descriptor retrieval usually needs no additional configuration, however it can be configured in the [IDP HttpClient sub element](https://www.keycloak.org/docs/latest/securing_apps/index.html#_sp-idp-httpclient).

It is also possible to specify multiple keys for signature verification. This is done by declaring multiple Key elements within Keys sub element that have `signing` attribute set to `true`. This is useful for example in situation when the IDP signing keys are rotated: There is usually a transition period when new SAML protocol messages and assertions are signed with the new key but those signed by previous key should still be accepted.

It is not possible to configure Keycloak to both obtain the keys for signature verification automatically and define additional static signature verification keys.

```
       <IDP entityID="idp">
            ...
            <Keys>
                <Key signing="true">
                    <KeyStore resource="/WEB-INF/keystore.jks" password="store123">
                        <Certificate alias="demo"/>
                    </KeyStore>
                </Key>
            </Keys>
        </IDP>
```

##### IDP HttpClient sub element {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/general-config/idp_httpclient_subelement.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/general-config/idp_httpclient_subelement.adoc)

The `HttpClient` optional sub element defines the properties of HTTP client used for automatic obtaining of certificates containing public keys for IDP signature verification via SAML descriptor of the IDP when [enabled](https://www.keycloak.org/docs/latest/securing_apps/index.html#_sp-idp-keys-automatic).

```
<HttpClient connectionPoolSize="10"
            disableTrustManager="false"
            allowAnyHostname="false"
            clientKeystore="classpath:keystore.jks"
            clientKeystorePassword="pwd"
            truststore="classpath:truststore.jks"
            truststorePassword="pwd"
            proxyUrl="http://proxy/" />
```

- connectionPoolSize

  Adapters will make separate HTTP invocations to the Keycloak server to turn an access code into an access token. This config option defines how many connections to the Keycloak server should be pooled. This is *OPTIONAL*. The default value is `10`.

- disableTrustManager

  If the Keycloak server requires HTTPS and this config option is set to `true` you do not have to specify a truststore. This setting should only be used during development and **never** in production as it will disable verification of SSL certificates. This is *OPTIONAL*. The default value is `false`.

- allowAnyHostname

  If the Keycloak server requires HTTPS and this config option is set to `true` the Keycloak server’s certificate is validated via the truststore, but host name validation is not done. This setting should only be used during development and **never** in production as it will partly disable verification of SSL certificates. This seting may be useful in test environments. This is *OPTIONAL*. The default value is `false`.

- truststore

  The value is the file path to a truststore file. If you prefix the path with `classpath:`, then the truststore will be obtained from the deployment’s classpath instead. Used for outgoing HTTPS communications to the Keycloak server. Client making HTTPS requests need a way to verify the host of the server they are talking to. This is what the trustore does. The keystore contains one or more trusted host certificates or certificate authorities. You can create this truststore by extracting the public certificate of the Keycloak server’s SSL keystore. This is *REQUIRED* unless `disableTrustManager` is `true`.

- truststorePassword

  Password for the truststore. This is *REQUIRED* if `truststore` is set and the truststore requires a password.

- clientKeystore

  This is the file path to a keystore file. This keystore contains client certificate for two-way SSL when the adapter makes HTTPS requests to the Keycloak server. This is *OPTIONAL*.

- clientKeystorePassword

  Password for the client keystore and for the client’s key. This is *REQUIRED* if `clientKeystore` is set.

- proxyUrl

  URL to HTTP proxy to use for HTTP connections. This is *OPTIONAL*.

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/saml-jboss-adapter.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/saml-jboss-adapter.adoc)

#### 3.1.2. JBoss EAP/WildFly Adapter {#}

To be able to secure WAR apps deployed on JBoss EAP or WildFly, you must install and configure the Keycloak SAML Adapter Subsystem.

You then provide a keycloak config, `/WEB-INF/keycloak-saml.xml` file in your WAR and change the auth-method to KEYCLOAK-SAML within web.xml. Both methods are described in this section.

##### Adapter Installation {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/jboss-adapter/jboss_adapter_installation.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/jboss-adapter/jboss_adapter_installation.adoc)

Each adapter is a separate download on the Keycloak download site.

|      | We only test and maintain adapter with the most recent version of WildFly available upon the release. Once new version of WildFly is released, the current adapters become deprecated and support for them will be removed after next WildFly release. The other alternative is to switch your applications from WildFly to the JBoss EAP, as the JBoss EAP adapter is supported for much longer period. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Install on WildFly 9 or newer or on JBoss EAP 7:

```
$ cd $WILDFLY_HOME
$ unzip keycloak-saml-wildfly-adapter-dist.zip
```

Install on JBoss EAP 6.x:

```
$ cd $JBOSS_HOME
$ unzip keycloak-saml-eap6-adapter-dist.zip
```

These zip files create new JBoss Modules specific to the WildFly/JBoss EAP SAML Adapter within your WildFly or JBoss EAP distro.

After adding the modules, you must then enable the Keycloak SAML Subsystem within your app server’s server configuration: `domain.xml` or `standalone.xml`.

There is a CLI script that will help you modify your server configuration. Start the server and run the script from the server’s bin directory:

WildFly 11 or newer

```
$ cd $JBOSS_HOME
$ ./bin/jboss-cli.sh -c --file=bin/adapter-elytron-install-saml.cli
```

WildFly 10 and older

```
$ cd $JBOSS_HOME
$ /bin/boss-cli.sh -c --file=bin/adapter-install-saml.cli
```

|      | It is possible to use the legacy non-Elytron adapter on WildFly 11 or newer as well, meaning you can use `adapter-install-saml.cli` even on those versions. However, we recommend to use the newer Elytron adapter. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

The script will add the extension, subsystem, and optional security-domain as described below.

```
<server xmlns="urn:jboss:domain:1.4">

    <extensions>
        <extension module="org.keycloak.keycloak-saml-adapter-subsystem"/>
          ...
    </extensions>

    <profile>
        <subsystem xmlns="urn:jboss:domain:keycloak-saml:1.1"/>
         ...
    </profile>
```

The `keycloak` security domain should be used with EJBs and other components when you need the security context created in the secured web tier to be propagated to the EJBs (other EE component) you are invoking. Otherwise this configuration is optional.

```
<server xmlns="urn:jboss:domain:1.4">
 <subsystem xmlns="urn:jboss:domain:security:1.2">
    <security-domains>
...
      <security-domain name="keycloak">
         <authentication>
           <login-module code="org.keycloak.adapters.jboss.KeycloakLoginModule"
                         flag="required"/>
          </authentication>
      </security-domain>
    </security-domains>
```

For example, if you have a JAX-RS service that is an EJB within your WEB-INF/classes directory, you’ll want to annotate it with the `@SecurityDomain` annotation as follows:

```
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

@Path("customers")
@Stateless
@SecurityDomain("keycloak")
public class CustomerService {

    @EJB
    CustomerDB db;

    @GET
    @Produces("application/json")
    @NoCache
    @RolesAllowed("db_user")
    public List<String> getCustomers() {
        return db.getCustomers();
    }
}
```

We hope to improve our integration in the future so that you don’t have to specify the `@SecurityDomain` annotation when you want to propagate a keycloak security context to the EJB tier.

##### JBoss SSO {#}

WildFly has built-in support for single sign-on for web applications deployed to the same WildFly instance. This should not be enabled when using Keycloak.

#### 3.1.3. Installing JBoss EAP Adapter from an RPM {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/jboss-adapter/jboss-adapter-rpms.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/jboss-adapter/jboss-adapter-rpms.adoc)

Install the EAP 7 Adapters from an RPM:

|      | With Red Hat Enterprise Linux 7, the term channel was replaced with the term repository. In these instructions only the term repository is used. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

You must subscribe to the JBoss EAP 7 repository before you can install the EAP 7 adapters from an RPM.

Prerequisites

1. Ensure that your Red Hat Enterprise Linux system is registered to your account using Red Hat Subscription Manager. For more information see the [Red Hat Subscription Management documentation](https://access.redhat.com/documentation/en-us/red_hat_subscription_management/1/html-single/quick_registration_for_rhel/index).
2. If you are already subscribed to another JBoss EAP repository, you must unsubscribe from that repository first.

Using Red Hat Subscription Manager, subscribe to the JBoss EAP 7 repository using the following command. Replace <RHEL_VERSION> with either 6 or 7 depending on your Red Hat Enterprise Linux version.

```
$ sudo subscription-manager repos --enable=jb-eap-7-for-rhel-<RHEL_VERSION>-server-rpms
```

Install the EAP 7 adapters for SAML using the following command:

```
$ sudo yum install eap7-keycloak-saml-adapter-sso7_2
```

|      | The default EAP_HOME path for the RPM installation is /opt/rh/eap7/root/usr/share/wildfly. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Run the appropriate module installation script.

For the SAML module, enter the following command:

```
$ $EAP_HOME/bin/jboss-cli.sh -c --file=$EAP_HOME/bin/adapter-install-saml.cli
```

Your installation is complete.

Install the EAP 6 Adapters from an RPM:

|      | With Red Hat Enterprise Linux 7, the term channel was replaced with the term repository. In these instructions only the term repository is used. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

You must subscribe to the JBoss EAP 6 repository before you can install the EAP 6 adapters from an RPM.

Prerequisites

1. Ensure that your Red Hat Enterprise Linux system is registered to your account using Red Hat Subscription Manager. For more information see the [Red Hat Subscription Management documentation](https://access.redhat.com/documentation/en-us/red_hat_subscription_management/1/html-single/quick_registration_for_rhel/index).
2. If you are already subscribed to another JBoss EAP repository, you must unsubscribe from that repository first.

Using Red Hat Subscription Manager, subscribe to the JBoss EAP 6 repository using the following command. Replace <RHEL_VERSION> with either 6 or 7 depending on your Red Hat Enterprise Linux version.

```
$ sudo subscription-manager repos --enable=jb-eap-6-for-rhel-<RHEL_VERSION>-server-rpms
```

Install the EAP 6 adapters for SAML using the following command:

```
$ sudo yum install keycloak-saml-adapter-sso7_2-eap6
```

|      | The default EAP_HOME path for the RPM installation is /opt/rh/eap6/root/usr/share/wildfly. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Run the appropriate module installation script.

For the SAML module, enter the following command:

```
$ $EAP_HOME/bin/jboss-cli.sh -c --file=$EAP_HOME/bin/adapter-install-saml.cli
```

Your installation is complete.

##### Per WAR Configuration {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/jboss-adapter/required_per_war_configuration.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/jboss-adapter/required_per_war_configuration.adoc)

This section describes how to secure a WAR directly by adding config and editing files within your WAR package.

The first thing you must do is create a `keycloak-saml.xml` adapter config file within the `WEB-INF` directory of your WAR. The format of this config file is described in the [General Adapter Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml-general-config) section.

Next you must set the `auth-method` to `KEYCLOAK-SAML` in `web.xml`. You also have to use standard servlet security to specify role-base constraints on your URLs. Here’s an example *web.xml* file:

```
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">

        <module-name>customer-portal</module-name>

    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Admins</web-resource-name>
            <url-pattern>/admin/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>admin</role-name>
        </auth-constraint>
        <user-data-constraint>
            <transport-guarantee>CONFIDENTIAL</transport-guarantee>
        </user-data-constraint>
    </security-constraint>
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Customers</web-resource-name>
            <url-pattern>/customers/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>user</role-name>
        </auth-constraint>
        <user-data-constraint>
            <transport-guarantee>CONFIDENTIAL</transport-guarantee>
        </user-data-constraint>
    </security-constraint>

    <login-config>
        <auth-method>KEYCLOAK-SAML</auth-method>
        <realm-name>this is ignored currently</realm-name>
    </login-config>

    <security-role>
        <role-name>admin</role-name>
    </security-role>
    <security-role>
        <role-name>user</role-name>
    </security-role>
</web-app>
```

All standard servlet settings except the `auth-method` setting.

##### Securing WARs via Keycloak SAML Subsystem {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/jboss-adapter/securing_wars.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/jboss-adapter/securing_wars.adoc)

You do not have to crack open a WAR to secure it with Keycloak. Alternatively, you can externally secure it via the Keycloak SAML Adapter Subsystem. While you don’t have to specify KEYCLOAK-SAML as an `auth-method`, you still have to define the `security-constraints` in `web.xml`. You do not, however, have to create a `WEB-INF/keycloak-saml.xml` file. This metadata is instead defined within the XML in your server’s `domain.xml` or `standalone.xml` subsystem configuration section.

```
<extensions>
  <extension module="org.keycloak.keycloak-saml-adapter-subsystem"/>
</extensions>

<profile>
  <subsystem xmlns="urn:jboss:domain:keycloak-saml:1.1">
    <secure-deployment name="WAR MODULE NAME.war">
      <SP entityID="APPLICATION URL">
        ...
      </SP>
    </secure-deployment>
  </subsystem>
</profile>
```

The `secure-deployment` `name` attribute identifies the WAR you want to secure. Its value is the `module-name` defined in `web.xml` with `.war` appended. The rest of the configuration uses the same XML syntax as `keycloak-saml.xml`configuration defined in [General Adapter Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml-general-config).

An example configuration:

```
<subsystem xmlns="urn:jboss:domain:keycloak-saml:1.1">
  <secure-deployment name="saml-post-encryption.war">
    <SP entityID="http://localhost:8080/sales-post-enc/"
        sslPolicy="EXTERNAL"
        nameIDPolicyFormat="urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified"
        logoutPage="/logout.jsp"
        forceAuthentication="false">
      <Keys>
        <Key signing="true" encryption="true">
          <KeyStore resource="/WEB-INF/keystore.jks" password="store123">
            <PrivateKey alias="http://localhost:8080/sales-post-enc/" password="test123"/>
            <Certificate alias="http://localhost:8080/sales-post-enc/"/>
          </KeyStore>
        </Key>
      </Keys>
      <PrincipalNameMapping policy="FROM_NAME_ID"/>
      <RoleIdentifiers>
        <Attribute name="Role"/>
      </RoleIdentifiers>
      <IDP entityID="idp">
        <SingleSignOnService signRequest="true"
            validateResponseSignature="true"
            requestBinding="POST"
            bindingUrl="http://localhost:8080/auth/realms/saml-demo/protocol/saml"/>

        <SingleLogoutService
            validateRequestSignature="true"
            validateResponseSignature="true"
            signRequest="true"
            signResponse="true"
            requestBinding="POST"
            responseBinding="POST"
            postBindingUrl="http://localhost:8080/auth/realms/saml-demo/protocol/saml"
            redirectBindingUrl="http://localhost:8080/auth/realms/saml-demo/protocol/saml"/>
        <Keys>
          <Key signing="true" >
            <KeyStore resource="/WEB-INF/keystore.jks" password="store123">
              <Certificate alias="saml-demo"/>
            </KeyStore>
          </Key>
        </Keys>
      </IDP>
    </SP>
   </secure-deployment>
</subsystem>
```

#### 3.1.4. Tomcat SAML adapters {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/tomcat-adapter.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/tomcat-adapter.adoc)

To be able to secure WAR apps deployed on Tomcat 6, 7 and 8 you must install the Keycloak Tomcat 6, 7 or 8 SAML adapter into your Tomcat installation. You then have to provide some extra configuration in each WAR you deploy to Tomcat. Let’s go over these steps.

##### Adapter Installation {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/tomcat-adapter/tomcat_adapter_installation.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/tomcat-adapter/tomcat_adapter_installation.adoc)

Adapters are no longer included with the appliance or war distribution. Each adapter is a separate download on the Keycloak download site. They are also available as a maven artifact.

You must unzip the adapter distro into Tomcat’s `lib/` directory. Including adapter’s jars within your WEB-INF/lib directory will not work! The Keycloak SAML adapter is implemented as a Valve and valve code must reside in Tomcat’s main lib/ directory.

```
$ cd $TOMCAT_HOME/lib
$ unzip keycloak-saml-tomcat6-adapter-dist.zip
    or
$ unzip keycloak-saml-tomcat7-adapter-dist.zip
    or
$ unzip keycloak-saml-tomcat8-adapter-dist.zip
```

##### Per WAR Configuration {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/tomcat-adapter/tomcat_adapter_per_war_config.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/tomcat-adapter/tomcat_adapter_per_war_config.adoc)

This section describes how to secure a WAR directly by adding config and editing files within your WAR package.

The first thing you must do is create a `META-INF/context.xml` file in your WAR package. This is a Tomcat specific config file and you must define a Keycloak specific Valve.

```
<Context path="/your-context-path">
    <Valve className="org.keycloak.adapters.saml.tomcat.SamlAuthenticatorValve"/>
</Context>
```

Next you must create a `keycloak-saml.xml` adapter config file within the `WEB-INF` directory of your WAR. The format of this config file is described in the [General Adapter Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml-general-config) section.

Finally you must specify both a `login-config` and use standard servlet security to specify role-base constraints on your URLs. Here’s an example:

```
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">

        <module-name>customer-portal</module-name>

    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Customers</web-resource-name>
            <url-pattern>/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>user</role-name>
        </auth-constraint>
    </security-constraint>

    <login-config>
        <auth-method>BASIC</auth-method>
        <realm-name>this is ignored currently</realm-name>
    </login-config>

    <security-role>
        <role-name>admin</role-name>
    </security-role>
    <security-role>
        <role-name>user</role-name>
    </security-role>
</web-app>
```

#### 3.1.5. Jetty SAML Adapters {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/jetty-adapter.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/jetty-adapter.adoc)

To be able to secure WAR apps deployed on Jetty you must install the Keycloak Jetty 9.x SAML adapter into your Jetty installation. You then have to provide some extra configuration in each WAR you deploy to Jetty. Let’s go over these steps.

##### Jetty 9 Adapter Installation {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/jetty-adapter/jetty9_installation.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/jetty-adapter/jetty9_installation.adoc)

Keycloak has a separate SAML adapter for Jetty 9.x. You then have to provide some extra configuration in each WAR you deploy to Jetty. Let’s go over these steps.

Adapters are no longer included with the appliance or war distribution. Each adapter is a separate download on the Keycloak download site. They are also available as a maven artifact.

You must unzip the Jetty 9.x distro into Jetty 9.x’s root directory. Including adapter’s jars within your WEB-INF/lib directory will not work!

```
$ cd $JETTY_HOME
$ unzip keycloak-saml-jetty92-adapter-dist.zip
```

Next, you will have to enable the keycloak module for your jetty.base.

```
$ cd your-base
$ java -jar $JETTY_HOME/start.jar --add-to-startd=keycloak
```

##### Jetty 9 Per WAR Configuration {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/jetty-adapter/jetty9_per_war_config.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/jetty-adapter/jetty9_per_war_config.adoc)

This section describes how to secure a WAR directly by adding config and editing files within your WAR package.

The first thing you must do is create a `WEB-INF/jetty-web.xml` file in your WAR package. This is a Jetty specific config file and you must define a Keycloak specific authenticator within it.

```
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Mort Bay Consulting//DTD Configure//EN" "http://www.eclipse.org/jetty/configure_9_0.dtd">
<Configure class="org.eclipse.jetty.webapp.WebAppContext">
    <Get name="securityHandler">
        <Set name="authenticator">
            <New class="org.keycloak.adapters.saml.jetty.KeycloakSamlAuthenticator">
            </New>
        </Set>
    </Get>
</Configure>
```

Next you must create a `keycloak-saml.xml` adapter config file within the `WEB-INF` directory of your WAR. The format of this config file is described in the [General Adapter Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml-general-config) section.

Finally you must specify both a `login-config` and use standard servlet security to specify role-base constraints on your URLs. Here’s an example:

```
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">

        <module-name>customer-portal</module-name>

    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Customers</web-resource-name>
            <url-pattern>/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>user</role-name>
        </auth-constraint>
        <user-data-constraint>
            <transport-guarantee>CONFIDENTIAL</transport-guarantee>
        </user-data-constraint>
    </security-constraint>

    <login-config>
        <auth-method>BASIC</auth-method>
        <realm-name>this is ignored currently</realm-name>
    </login-config>

    <security-role>
        <role-name>admin</role-name>
    </security-role>
    <security-role>
        <role-name>user</role-name>
    </security-role>
</web-app>
```

#### 3.1.6. Java Servlet Filter Adapter {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/servlet-filter-adapter.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/servlet-filter-adapter.adoc)

If you want to use SAML with a Java servlet application that doesn’t have an adapter for that servlet platform, you can opt to use the servlet filter adapter that Keycloak has. This adapter works a little differently than the other adapters. You still have to specify a `/WEB-INF/keycloak-saml.xml` file as defined in the [General Adapter Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml-general-config) section, but you do not define security constraints in *web.xml*. Instead you define a filter mapping using the Keycloak servlet filter adapter to secure the url patterns you want to secure.

|      | Backchannel logout works a bit differently than the standard adapters. Instead of invalidating the http session it instead marks the session ID as logged out. There’s just no way of arbitrarily invalidating an http session based on a session ID. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

|      | Backchannel logout does not currently work when you have a clustered application that uses the SAML filter. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

```
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">

        <module-name>customer-portal</module-name>

    <filter>
        <filter-name>Keycloak Filter</filter-name>
        <filter-class>org.keycloak.adapters.saml.servlet.SamlFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>Keycloak Filter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
</web-app>
```

The Keycloak filter has the same configuration parameters available as the other adapters except you must define them as filter init params instead of context params.

You can define multiple filter mappings if you have various different secure and unsecure url patterns.

|      | You must have a filter mapping that covers `/saml`. This mapping covers all server callbacks. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

When registering SPs with an IdP, you must register `http[s]://hostname/{context-root}/saml` as your Assert Consumer Service URL and Single Logout Service URL.

To use this filter, include this maven artifact in your WAR poms:

```
<dependency>
   <groupId>org.keycloak</groupId>
   <artifactId>keycloak-saml-servlet-filter-adapter</artifactId>
   <version>6.0.1</version>
</dependency>
```

In order to use [Multi Tenancy](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml_multi_tenancy) the `keycloak.config.resolver` parameter should be passed as a filter parameter.

```
    <filter>
        <filter-name>Keycloak Filter</filter-name>
        <filter-class>org.keycloak.adapters.saml.servlet.SamlFilter</filter-class>
        <init-param>
            <param-name>keycloak.config.resolver</param-name>
            <param-value>example.SamlMultiTenantResolver</param-value>
        </init-param>
    </filter>
```

#### 3.1.7. Registering with an Identity Provider {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/idp-registration.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/idp-registration.adoc)

For each servlet-based adapter, the endpoint you register for the assert consumer service URL and single logout service must be the base URL of your servlet application with `/saml` appended to it, that is, `https://example.com/contextPath/saml`.

#### 3.1.8. Logout {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/logout.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/logout.adoc)

There are multiple ways you can logout from a web application. For Java EE servlet containers, you can call `HttpServletRequest.logout()`. For any other browser application, you can point the browser at any url of your web application that has a security constraint and pass in a query parameter GLO, i.e. `http://myapp?GLO=true`. This will log you out if you have an SSO session with your browser.

##### Logout in Clustered Environment {#}

Internally, the SAML adapter stores a mapping between the SAML session index, principal name (when known), and HTTP session ID. This mapping can be maintained in JBoss application server family (WildFly 10/11, EAP 6/7) across cluster for distributable applications. As a precondition, the HTTP sessions need to be distributed across cluster (i.e. application is marked with `<distributable/>` tag in application’s `web.xml`).

To enable the functionality, add the following section to your `/WEB_INF/web.xml` file:

For EAP 7, WildFly 10/11:

```
<context-param>
    <param-name>keycloak.sessionIdMapperUpdater.classes</param-name>
    <param-value>org.keycloak.adapters.saml.wildfly.infinispan.InfinispanSessionCacheIdMapperUpdater</param-value>
</context-param>
```

For EAP 6:

```
<context-param>
    <param-name>keycloak.sessionIdMapperUpdater.classes</param-name>
    <param-value>org.keycloak.adapters.saml.jbossweb.infinispan.InfinispanSessionCacheIdMapperUpdater</param-value>
</context-param>
```

If the session cache of the deployment is named `*deployment-cache*`, the cache used for SAML mapping will be named as `*deployment-cache*.ssoCache`. The name of the cache can be overridden by a context parameter`keycloak.sessionIdMapperUpdater.infinispan.cacheName`. The cache container containing the cache will be the same as the one containing the deployment session cache, but can be overridden by a context parameter`keycloak.sessionIdMapperUpdater.infinispan.containerName`.

By default, the configuration of the SAML mapping cache will be derived from session cache. The configuration can be manually overridden in cache configuration section of the server just the same as other caches.

Currently, to provide reliable service, it is recommended to use replicated cache for the SAML session cache. Using distributed cache may lead to results where the SAML logout request would land to a node with no access to SAML session index to HTTP session mapping which would lead to unsuccessful logout.

##### Logout in Cross DC Scenario {#}

The cross DC scenario only applies to WildFly 10 and higher, and EAP 7 and higher.

Special handling is needed for handling sessions that span multiple data centers. Imagine the following scenario:

1. Login requests are handled within cluster in data center 1.
2. Admin issues logout request for a particular SAML session, the request lands in data center 2.

The data center 2 has to log out all sessions that are present in data center 1 (and all other data centers that share HTTP sessions).

To cover this case, the SAML session cache described [above](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml_logout_in_cluster) needs to be replicated not only within individual clusters but across all the data centers e.g. [via standalone Infinispan/JDG server](https://access.redhat.com/documentation/en-us/red_hat_data_grid/6.6/html/administration_and_configuration_guide/chap-externalize_sessions#Externalize_HTTP_Session_from_JBoss_EAP_6.x_to_JBoss_Data_Grid):

1. A cache has to be added to the standalone Infinispan/JDG server.
2. The cache from previous item has to be added as a remote store for the respective SAML session cache.

Once remote store is found to be present on SAML session cache during deployment, it is watched for changes and the local SAML session cache is updated accordingly.

#### 3.1.9. Obtaining Assertion Attributes {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/assertion-api.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/assertion-api.adoc)

After a successful SAML login, your application code may want to obtain attribute values passed with the SAML assertion. `HttpServletRequest.getUserPrincipal()` returns a `Principal` object that you can typecast into a Keycloak specific class called `org.keycloak.adapters.saml.SamlPrincipal`. This object allows you to look at the raw assertion and also has convenience functions to look up attribute values.

```
package org.keycloak.adapters.saml;

public class SamlPrincipal implements Serializable, Principal {
    /**
     * Get full saml assertion
     *
     * @return
     */
    public AssertionType getAssertion() {
       ...
    }

    /**
     * Get SAML subject sent in assertion
     *
     * @return
     */
    public String getSamlSubject() {
        ...
    }

    /**
     * Subject nameID format
     *
     * @return
     */
    public String getNameIDFormat() {
        ...
    }

    @Override
    public String getName() {
        ...
    }

    /**
     * Convenience function that gets Attribute value by attribute name
     *
     * @param name
     * @return
     */
    public List<String> getAttributes(String name) {
        ...

    }

    /**
     * Convenience function that gets Attribute value by attribute friendly name
     *
     * @param friendlyName
     * @return
     */
    public List<String> getFriendlyAttributes(String friendlyName) {
        ...
    }

    /**
     * Convenience function that gets first  value of an attribute by attribute name
     *
     * @param name
     * @return
     */
    public String getAttribute(String name) {
        ...
    }

    /**
     * Convenience function that gets first  value of an attribute by attribute name
     *
     *
     * @param friendlyName
     * @return
     */
    public String getFriendlyAttribute(String friendlyName) {
        ...
    }

    /**
     * Get set of all assertion attribute names
     *
     * @return
     */
    public Set<String> getAttributeNames() {
        ...
    }

    /**
     * Get set of all assertion friendly attribute names
     *
     * @return
     */
    public Set<String> getFriendlyNames() {
        ...
    }
}
```

#### 3.1.10. Error Handling {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/error_handling.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/error_handling.adoc)

Keycloak has some error handling facilities for servlet based client adapters. When an error is encountered in authentication, the client adapter will call `HttpServletResponse.sendError()`. You can set up an `error-page` within your `web.xml` file to handle the error however you want. The client adapter can throw 400, 401, 403, and 500 errors.

```
<error-page>
    <error-code>403</error-code>
    <location>/ErrorHandler</location>
</error-page>
```

The client adapter also sets an `HttpServletRequest` attribute that you can retrieve. The attribute name is `org.keycloak.adapters.spi.AuthenticationError`. Typecast this object to: `org.keycloak.adapters.saml.SamlAuthenticationError`. This class can tell you exactly what happened. If this attribute is not set, then the adapter was not responsible for the error code.

```
public class SamlAuthenticationError implements AuthenticationError {
    public static enum Reason {
        EXTRACTION_FAILURE,
        INVALID_SIGNATURE,
        ERROR_STATUS
    }

    public Reason getReason() {
        return reason;
    }
    public StatusResponseType getStatus() {
        return status;
    }
}
```

#### 3.1.11. Troubleshooting {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/debugging.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/debugging.adoc)

The best way to troubleshoot problems is to turn on debugging for SAML in both the client adapter and Keycloak Server. Using your logging framework, set the log level to `DEBUG` for the `org.keycloak.saml`package. Turning this on allows you to see the SAML requests and response documents being sent to and from the server.

#### 3.1.12. Multi Tenancy {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/multi-tenancy.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/multi-tenancy.adoc)

SAML offers the same functionality as OIDC for [Multi Tenancy](https://www.keycloak.org/docs/latest/securing_apps/index.html#_multi_tenancy), meaning that a single target application (WAR) can be secured with multiple Keycloak realms. The realms can be located on the same Keycloak instance or on different instances.

To do this, the application must have multiple `keycloak-saml.xml` adapter configuration files.

While you could have multiple instances of your WAR with different adapter configuration files deployed to different context-paths, this may be inconvenient and you may also want to select the realm based on something other than context-path.

Keycloak makes it possible to have a custom config resolver, so you can choose which adapter config is used for each request. In SAML, the configuration is only interesting in the login processing; once the user is logged in, the session is authenticated and it does not matter if the `keycloak-saml.xml` returned is different. For that reason, returning the same configuration for the same session is the correct way to go.

To achieve this, create an implementation of `org.keycloak.adapters.saml.SamlConfigResolver`. The following example uses the `Host` header to locate the proper configuration and load it and the associated elements from the applications’s Java classpath:

```
package example;

import java.io.InputStream;
import org.keycloak.adapters.saml.SamlConfigResolver;
import org.keycloak.adapters.saml.SamlDeployment;
import org.keycloak.adapters.saml.config.parsers.DeploymentBuilder;
import org.keycloak.adapters.saml.config.parsers.ResourceLoader;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.saml.common.exceptions.ParsingException;

public class SamlMultiTenantResolver implements SamlConfigResolver {

    @Override
    public SamlDeployment resolve(HttpFacade.Request request) {
        String host = request.getHeader("Host");
        String realm = null;
        if (host.contains("tenant1")) {
            realm = "tenant1";
        } else if (host.contains("tenant2")) {
            realm = "tenant2";
        } else {
            throw new IllegalStateException("Not able to guess the keycloak-saml.xml to load");
        }

        InputStream is = getClass().getResourceAsStream("/" + realm + "-keycloak-saml.xml");
        if (is == null) {
            throw new IllegalStateException("Not able to find the file /" + realm + "-keycloak-saml.xml");
        }

        ResourceLoader loader = new ResourceLoader() {
            @Override
            public InputStream getResourceAsStream(String path) {
                return getClass().getResourceAsStream(path);
            }
        };

        try {
            return new DeploymentBuilder().build(is, loader);
        } catch (ParsingException e) {
            throw new IllegalStateException("Cannot load SAML deployment", e);
        }
    }
}
```

You must also configure which `SamlConfigResolver` implementation to use with the `keycloak.config.resolver`context-param in your `web.xml`:

```
<web-app>
    ...
    <context-param>
        <param-name>keycloak.config.resolver</param-name>
        <param-value>example.SamlMultiTenantResolver</param-value>
    </context-param>
</web-app>
```

#### 3.1.13. Migration from older versions {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/MigrationFromOlderVersions.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/MigrationFromOlderVersions.adoc)

##### Migrating to 1.9.0 {#}

###### SAML SP Client Adapter Changes {#}

Keycloak SAML SP Client Adapter now requires a specific endpoint, `/saml` to be registered with your IdP. The SamlFilter must also be bound to /saml in addition to any other binding it has. This had to be done because SAML POST binding would eat the request input stream and this would be really bad for clients that relied on it.

### 3.2. mod_auth_mellon Apache HTTPD Module {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/mod-auth-mellon.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/mod-auth-mellon.adoc)

The [mod_auth_mellon](https://github.com/UNINETT/mod_auth_mellon) module is an Apache HTTPD plugin for SAML. If your language/environment supports using Apache HTTPD as a proxy, then you can use mod_auth_mellon to secure your web application with SAML. For more details on this module see the *mod_auth_mellon* GitHub repo.

To configure mod_auth_mellon you’ll need:

- An Identity Provider (IdP) entity descriptor XML file, which describes the connection to Keycloak or another SAML IdP
- An SP entity descriptor XML file, which describes the SAML connections and configuration for the application you are securing.
- A private key PEM file, which is a text file in the PEM format that defines the private key the application uses to sign documents.
- A certificate PEM file, which is a text file that defines the certificate for your application.
- mod_auth_mellon-specific Apache HTTPD module configuration.

If you have already defined and registered the client application within a realm on the Keycloak application server, Keycloak can generate all the files you need except the Apache HTTPD module configuration.

To generate the Apache HTTPD module configuration, complete the following steps:

1. Go to the Installation page of your SAML client and select the Mod Auth Mellon files option.

   mod_auth_mellon config download

   ![mod auth mellon config download](assets/mod-auth-mellon-config-download.png)

2. Click **Download** to download a zip file that contains the XML descriptor and PEM files you need.

#### 3.2.1. Configuring mod_auth_mellon with Keycloak {#}

There are two hosts involved:

- The host on which Keycloak is running, which will be referred to as $idp_host because Keycloak is a SAML identity provider (IdP).
- The host on which the web application is running, which will be referred to as $sp_host. In SAML an application using an IdP is called a service provider (SP).

All of the following steps need to performed on $sp_host with root privileges.

##### Installing the Packages {#}

To install the necessary packages, you will need:

- Apache Web Server (httpd)
- Mellon SAML SP add-on module for Apache
- Tools to create X509 certificates

To install the necessary packages, run this command:

```
yum install httpd mod_auth_mellon mod_ssl openssl
```

##### Creating a Configuration Directory for Apache SAML {#}

It is advisable to keep configuration files related to Apache’s use of SAML in one location.

Create a new directory named saml2 located under the Apache configuration root /etc/httpd:

```
mkdir /etc/httpd/saml2
```

##### Configuring the Mellon Service Provider {#}

Configuration files for Apache add-on modules are located in the /etc/httpd/conf.d directory and have a file name extension of .conf. You need to create the /etc/httpd/conf.d/mellon.conf file and place Mellon’s configuration directives in it.

Mellon’s configuration directives can roughly be broken down into two classes of information:

- Which URLs to protect with SAML authentication
- What SAML parameters will be used when a protected URL is referenced.

Apache configuration directives typically follow a hierarchical tree structure in the URL space, which are known as locations. You need to specify one or more URL locations for Mellon to protect. You have flexibility in how you add the configuration parameters that apply to each location. You can either add all the necessary parameters to the location block or you can add Mellon parameters to a common location high up in the URL location hierarchy that specific protected locations inherit (or some combination of the two). Since it is common for an SP to operate in the same way no matter which location triggers SAML actions, the example configuration used here places common Mellon configuration directives in the root of the hierarchy and then specific locations to be protected by Mellon can be defined with minimal directives. This strategy avoids duplicating the same parameters for each protected location.

This example has just one protected location: https://$sp_host/protected.

To configure the Mellon service provider, complete the following steps:

1. Create the file /etc/httpd/conf.d/mellon.conf with this content:

```
 <Location / >
    MellonEnable info
    MellonEndpointPath /mellon/
    MellonSPMetadataFile /etc/httpd/saml2/mellon_metadata.xml
    MellonSPPrivateKeyFile /etc/httpd/saml2/mellon.key
    MellonSPCertFile /etc/httpd/saml2/mellon.crt
    MellonIdPMetadataFile /etc/httpd/saml2/idp_metadata.xml
 </Location>
 <Location /private >
    AuthType Mellon
    MellonEnable auth
    Require valid-user
 </Location>
```

|      | Some of the files referenced in the code above are created in later steps. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

##### Creating the Service Provider Metadata {#}

In SAML IdPs and SPs exchange SAML metadata, which is in XML format. The schema for the metadata is a standard, thus assuring participating SAML entities can consume each other’s metadata. You need:

- Metadata for the IdP that the SP utilizes
- Metadata describing the SP provided to the IdP

One of the components of SAML metadata is X509 certificates. These certificates are used for two purposes:

- Sign SAML messages so the receiving end can prove the message originated from the expected party.
- Encrypt the message during transport (seldom used because SAML messages typically occur on TLS-protected transports)

You can use your own certificates if you already have a Certificate Authority (CA) or you can generate a self-signed certificate. For simplicity in this example a self-signed certificate is used.

Because Mellon’s SP metadata must reflect the capabilities of the installed version of mod_auth_mellon, must be valid SP metadata XML, and must contain an X509 certificate (whose creation can be obtuse unless you are familiar with X509 certificate generation) the most expedient way to produce the SP metadata is to use a tool included in the mod_auth_mellon package (mellon_create_metadata.sh). The generated metadata can always be edited later because it is a text file. The tool also creates your X509 key and certificate.

SAML IdPs and SPs identify themselves using a unique name known as an EntityID. To use the Mellon metadata creation tool you need:

- The EntityID, which is typically the URL of the SP, and often the URL of the SP where the SP metadata can be retrieved
- The URL where SAML messages for the SP will be consumed, which Mellon calls the MellonEndPointPath.

To create the SP metadata, complete the following steps:

1. Create a few helper shell variables:

   ```
   fqdn=`hostname`
   mellon_endpoint_url="https://${fqdn}/mellon"
   mellon_entity_id="${mellon_endpoint_url}/metadata"
   file_prefix="$(echo "$mellon_entity_id" | sed 's/[^A-Za-z.]/_/g' | sed 's/__*/_/g')"
   ```

2. Invoke the Mellon metadata creation tool by running this command:

   ```
   /usr/libexec/mod_auth_mellon/mellon_create_metadata.sh $mellon_entity_id $mellon_endpoint_url
   ```

3. Move the generated files to their destination (referenced in the /etc/httpd/conf.d/mellon.conf file created above):

   ```
   mv ${file_prefix}.cert /etc/httpd/saml2/mellon.crt
   mv ${file_prefix}.key /etc/httpd/saml2/mellon.key
   mv ${file_prefix}.xml /etc/httpd/saml2/mellon_metadata.xml
   ```

##### Adding the Mellon Service Provider to the Keycloak Identity Provider {#}

Assumption: The Keycloak IdP has already been installed on the $idp_host.

Keycloak supports multiple tenancy where all users, clients, and so on are grouped in what is called a realm. Each realm is independent of other realms. You can use an existing realm in your Keycloak, but this example shows how to create a new realm called test_realm and use that realm.

All these operations are performed using the Keycloak administration web console. You must have the admin username and password for $idp_host.

To complete the following steps:

1. Open the Admin Console and log on by entering the admin username and password.

   After logging into the administration console there will be an existing realm. When Keycloak is first set up a root realm, master, is created by default. Any previously created realms are listed in the upper left corner of the administration console in a drop-down list.

2. From the realm drop-down list select **Add realm**.

3. In the Name field type `test_realm` and click **Create**.

###### Adding the Mellon Service Provider as a Client of the Realm {#}

In Keycloak SAML SPs are known as clients. To add the SP we must be in the Clients section of the realm.

1. Click the Clients menu item on the left and click **Create** in the upper right corner to create a new client.

###### Adding the Mellon SP Client {#}

To add the Mellon SP client, complete the following steps:

1. Set the client protocol to SAML. From the Client Protocol drop down list, select **saml**.
2. Provide the Mellon SP metadata file created above (/etc/httpd/saml2/mellon_metadata.xml). Depending on where your browser is running you might have to copy the SP metadata from $sp_host to the machine on which your browser is running so the browser can find the file.
3. Click **Save**.

###### Editing the Mellon SP Client {#}

There are several client configuration parameters we suggest setting:

- Ensure "Force POST Binding" is On.
- Add paosResponse to the Valid Redirect URIs list:
  1. Copy the postResponse URL in "Valid Redirect URIs" and paste it into the empty add text fields just below the "+".
  2. Change "postResponse" to "paosResponse". (The paosResponse URL is needed for SAML ECP.)
  3. Click **Save** at the bottom.

Many SAML SPs determine authorization based on a user’s membership in a group. The Keycloak IdP can manage user group information but it does not supply the user’s groups unless the IdP is configured to supply it as a SAML attribute.

To configure the IdP to supply the user’s groups as as a SAML attribute, complete the following steps:

1. Click the Mappers tab of the client.
2. In the upper right corner of the Mappers page, click **Create**.
3. From the Mapper Type drop-down list select **Group list**.
4. Set Name to "group list".
5. Set the SAML attribute name to "groups".
6. Click **Save**.

The remaining steps are performed on $sp_host.

###### Retrieving the Identity Provider Metadata {#}

Now that you have created the realm on the IdP you need to retrieve the IdP metadata associated with it so the Mellon SP recognizes it. In the /etc/httpd/conf.d/mellon.conf file created previously, the MellonIdPMetadataFile is specified as /etc/httpd/saml2/idp_metadata.xml but until now that file has not existed on $sp_host. To get that file we will retrieve it from the IdP.

1. Retrieve the file from the IdP by substituting $idp_host with the correct value:

   ```
   curl -k -o /etc/httpd/saml2/idp_metadata.xml \
   https://$idp_host/auth/realms/test_realm/protocol/saml/descriptor
   ```

   Mellon is now fully configured.

2. To run a syntax check for Apache configuration files:

   ```
   apachectl configtest
   ```

   |      | Configtest is equivalent to the -t argument to apachectl. If the configuration test shows any errors, correct them before proceeding. |
   | ---- | ------------------------------------------------------------ |
   |      |                                                              |

3. Restart the Apache server:

   ```
   systemctl restart httpd.service
   ```

You have now set up both Keycloak as a SAML IdP in the test_realm and mod_auth_mellon as SAML SP protecting the URL $sp_host/protected (and everything beneath it) by authenticating against the `$idp_host` IdP.

## 4. Docker Registry Configuration {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/docker/docker-overview.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/docker/docker-overview.adoc)

|      | Docker authentication is disabled by default. To enable see [Profiles](https://www.keycloak.org/docs/6.0/server_installation/#profiles). |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

This section describes how you can configure a Docker registry to use Keycloak as its authentication server.

For more information on how to set up and configure a Docker registry, see the [Docker Registry Configuration Guide](https://docs.docker.com/registry/configuration/).

### 4.1. Docker Registry Configuration File Installation {#}

For users with more advanced Docker registry configurations, it is generally recommended to provide your own registry configuration file. The Keycloak Docker provider supports this mechanism via the *Registry Config File* Format Option. Choosing this option will generate output similar to the following:

```
auth:
  token:
    realm: http://localhost:8080/auth/realms/master/protocol/docker-v2/auth
    service: docker-test
    issuer: http://localhost:8080/auth/realms/master
```

This output can then be copied into any existing registry config file. See the [registry config file specification](https://docs.docker.com/registry/configuration/) for more information on how the file should be set up, or start with [a basic example](https://github.com/docker/distribution/blob/master/cmd/registry/config-example.yml).

|      | Don’t forget to configure the `rootcertbundle` field with the location of the Keycloak realm’s pulic certificate. The auth configuration will not work without this argument. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 4.2. Docker Registry Environment Variable Override Installation {#}

Often times it is appropriate to use a simple environment variable override for develop or POC Docker registries. While this approach is usually not recommended for production use, it can be helpful when one requires quick-and-dirty way to stand up a registry. Simply use the *Variable Override* Format Option from the client installation tab, and an output should appear like the one below:

```
REGISTRY_AUTH_TOKEN_REALM: http://localhost:8080/auth/realms/master/protocol/docker-v2/auth
REGISTRY_AUTH_TOKEN_SERVICE: docker-test
REGISTRY_AUTH_TOKEN_ISSUER: http://localhost:8080/auth/realms/master
```

|      | Don’t forget to configure the `REGISTRY_AUTH_TOKEN_ROOTCERTBUNDLE` override with the location of the Keycloak realm’s pulic certificate. The auth configuration will not work without this argument. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 4.3. Docker Compose YAML File {#}

|      | This installation method is meant to be an easy way to get a docker registry authenticating against a Keycloak server. It is intended for development purposes only and should never be used in a production or production-like environment. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

The zip file installation mechanism provides a quickstart for developers who want to understand how the Keycloak server can interact with the Docker registry. In order to configure:

1. From the desired realm, create a client configuration. At this point you won’t have a Docker registry - the quickstart will take care of that part.
2. Choose the "Docker Compose YAML" option from the installation tab and download the .zip file
3. Unzip the archive to the desired location, and open the directory.
4. Start the Docker registry with `docker-compose up`

|      | it is recommended that you configure the Docker registry client in a realm other than 'master', since the HTTP Basic auth flow will not present forms. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Once the above configuration has taken place, and the keycloak server and Docker registry are running, docker authentication should be successful:

```
[user ~]#??docker login localhost:5000 -u $username
Password: *******
Login Succeeded
```

## 5. Client Registration {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/client-registration.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/client-registration.adoc)

In order for an application or service to utilize Keycloak it has to register a client in Keycloak. An admin can do this through the admin console (or admin REST endpoints), but clients can also register themselves through the Keycloak client registration service.

The Client Registration Service provides built-in support for Keycloak Client Representations, OpenID Connect Client Meta Data and SAML Entity Descriptors. The Client Registration Service endpoint is `/auth/realms/<realm>/clients-registrations/<provider>`.

The built-in supported `providers` are:

- default - Keycloak Client Representation (JSON)
- install - Keycloak Adapter Configuration (JSON)
- openid-connect - OpenID Connect Client Metadata Description (JSON)
- saml2-entity-descriptor - SAML Entity Descriptor (XML)

The following sections will describe how to use the different providers.

### 5.1. Authentication {#}

To invoke the Client Registration Services you usually need a token. The token can be a bearer token, an initial access token or a registration access token. There is an alternative to register new client without any token as well, but then you need to configure Client Registration Policies (see below).

#### 5.1.1. Bearer Token {#}

The bearer token can be issued on behalf of a user or a Service Account. The following permissions are required to invoke the endpoints (see [Server Administration Guide](https://www.keycloak.org/docs/6.0/server_admin/) for more details):

- create-client or manage-client - To create clients
- view-client or manage-client - To view clients
- manage-client - To update or delete client

If you are using a bearer token to create clients it’s recommend to use a token from a Service Account with only the `create-client` role (see [Server Administration Guide](https://www.keycloak.org/docs/6.0/server_admin/) for more details).

#### 5.1.2. Initial Access Token {#}

The recommended approach to registering new clients is by using initial access tokens. An initial access token can only be used to create clients and has a configurable expiration as well as a configurable limit on how many clients can be created.

An initial access token can be created through the admin console. To create a new initial access token first select the realm in the admin console, then click on `Realm Settings` in the menu on the left, followed by `Client Registration` in the tabs displayed in the page. Then finally click on `Initial Access Tokens` sub-tab.

You will now be able to see any existing initial access tokens. If you have access you can delete tokens that are no longer required. You can only retrieve the value of the token when you are creating it. To create a new token click on `Create`. You can now optionally add how long the token should be valid, also how many clients can be created using the token. After you click on `Save` the token value is displayed.

It is important that you copy/paste this token now as you won’t be able to retrieve it later. If you forget to copy/paste it, then delete the token and create another one.

The token value is used as a standard bearer token when invoking the Client Registration Services, by adding it to the Authorization header in the request. For example:

```
Authorization: bearer eyJhbGciOiJSUz...
```

#### 5.1.3. Registration Access Token {#}

When you create a client through the Client Registration Service the response will include a registration access token. The registration access token provides access to retrieve the client configuration later, but also to update or delete the client. The registration access token is included with the request in the same way as a bearer token or initial access token. Registration access tokens are only valid once, when it’s used the response will include a new token.

If a client was created outside of the Client Registration Service it won’t have a registration access token associated with it. You can create one through the admin console. This can also be useful if you loose the token for a particular client. To create a new token find the client in the admin console and click on `Credentials`. Then click on `Generate registration access token`.

### 5.2. Keycloak Representations {#}

The `default` client registration provider can be used to create, retrieve, update and delete a client. It uses Keycloak Client Representation format which provides support for configuring clients exactly as they can be configured through the admin console, including for example configuring protocol mappers.

To create a client create a Client Representation (JSON) then perform an HTTP POST request to `/auth/realms/<realm>/clients-registrations/default`.

It will return a Client Representation that also includes the registration access token. You should save the registration access token somewhere if you want to retrieve the config, update or delete the client later.

To retrieve the Client Representation perform an HTTP GET request to `/auth/realms/<realm>/clients-registrations/default/<client id>`.

It will also return a new registration access token.

To update the Client Representation perform an HTTP PUT request with the updated Client Representation to:`/auth/realms/<realm>/clients-registrations/default/<client id>`.

It will also return a new registration access token.

To delete the Client Representation perform an HTTP DELETE request to: `/auth/realms/<realm>/clients-registrations/default/<client id>`

### 5.3. Keycloak Adapter Configuration {#}

The `installation` client registration provider can be used to retrieve the adapter configuration for a client. In addition to token authentication you can also authenticate with client credentials using HTTP basic authentication. To do this include the following header in the request:

```
Authorization: basic BASE64(client-id + ':' + client-secret)
```

To retrieve the Adapter Configuration then perform an HTTP GET request to `/auth/realms/<realm>/clients-registrations/install/<client id>`.

No authentication is required for public clients. This means that for the JavaScript adapter you can load the client configuration directly from Keycloak using the above URL.

### 5.4. OpenID Connect Dynamic Client Registration {#}

Keycloak implements [OpenID Connect Dynamic Client Registration](https://openid.net/specs/openid-connect-registration-1_0.html), which extends [OAuth 2.0 Dynamic Client Registration Protocol](https://tools.ietf.org/html/rfc7591) and [OAuth 2.0 Dynamic Client Registration Management Protocol](https://tools.ietf.org/html/rfc7592).

The endpoint to use these specifications to register clients in Keycloak is `/auth/realms/<realm>/clients-registrations/openid-connect[/<client id>]`.

This endpoint can also be found in the OpenID Connect Discovery endpoint for the realm, `/auth/realms/<realm>/.well-known/openid-configuration`.

### 5.5. SAML Entity Descriptors {#}

The SAML Entity Descriptor endpoint only supports using SAML v2 Entity Descriptors to create clients. It doesn’t support retrieving, updating or deleting clients. For those operations the Keycloak representation endpoints should be used. When creating a client a Keycloak Client Representation is returned with details about the created client, including a registration access token.

To create a client perform an HTTP POST request with the SAML Entity Descriptor to `/auth/realms/<realm>/clients-registrations/saml2-entity-descriptor`.

### 5.6. Example using CURL {#}

The following example creates a client with the clientId `myclient` using CURL. You need to replace `eyJhbGciOiJSUz…` with a proper initial access token or bearer token.

```
curl -X POST \
    -d '{ "clientId": "myclient" }' \
    -H "Content-Type:application/json" \
    -H "Authorization: bearer eyJhbGciOiJSUz..." \
    http://localhost:8080/auth/realms/master/clients-registrations/default
```

### 5.7. Example using Java Client Registration API {#}

The Client Registration Java API makes it easy to use the Client Registration Service using Java. To use include the dependency `org.keycloak:keycloak-client-registration-api:>VERSION<` from Maven.

For full instructions on using the Client Registration refer to the JavaDocs. Below is an example of creating a client. You need to replace `eyJhbGciOiJSUz…` with a proper initial access token or bearer token.

```
String token = "eyJhbGciOiJSUz...";

ClientRepresentation client = new ClientRepresentation();
client.setClientId(CLIENT_ID);

ClientRegistration reg = ClientRegistration.create()
    .url("http://localhost:8080/auth", "myrealm")
    .build();

reg.auth(Auth.token(token));

client = reg.create(client);

String registrationAccessToken = client.getRegistrationAccessToken();
```

### 5.8. Client Registration Policies {#}

Keycloak currently supports 2 ways how can be new clients registered through Client Registration Service.

- Authenticated requests - Request to register new client must contain either `Initial Access Token` or `Bearer Token`as mentioned above.
- Anonymous requests - Request to register new client doesn’t need to contain any token at all

Anonymous client registration requests are very interesting and powerful feature, however you usually don’t want that anyone is able to register new client without any limitations. Hence we have `Client Registration Policy SPI`, which provide a way to limit who can register new clients and under which conditions.

In Keycloak admin console, you can click to `Client Registration` tab and then `Client Registration Policies` sub-tab. Here you will see what policies are configured by default for anonymous requests and what policies are configured for authenticated requests.

|      | The anonymous requests (requests without any token) are allowed just for creating (registration) of new clients. So when you register new client through anonymous request, the response will contain Registration Access Token, which must be used for Read, Update or Delete request of particular client. However using this Registration Access Token from anonymous registration will be then subject to Anonymous Policy too! This means that for example request for update client also needs to come from Trusted Host if you have `Trusted Hosts` policy. Also for example it won’t be allowed to disable `Consent Required` when updating client and when `Consent Required` policy is present etc. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Currently we have these policy implementations:

- Trusted Hosts Policy - You can configure list of trusted hosts and trusted domains. Request to Client Registration Service can be sent just from those hosts or domains. Request sent from some untrusted IP will be rejected. URLs of newly registered client must also use just those trusted hosts or domains. For example it won’t be allowed to set `Redirect URI`of client pointing to some untrusted host. By default, there is not any whitelisted host, so anonymous client registration is de-facto disabled.
- Consent Required Policy - Newly registered clients will have `Consent Allowed` switch enabled. So after successful authentication, user will always see consent screen when he needs to approve permissions (client scopes). It means that client won’t have access to any personal info or permission of user unless user approves it.
- Protocol Mappers Policy - Allows to configure list of whitelisted protocol mapper implementations. New client can’t be registered or updated if it contains some non-whitelisted protocol mapper. Note that this policy is used for authenticated requests as well, so even for authenticated request there are some limitations which protocol mappers can be used.
- Client Scope Policy - Allow to whitelist `Client Scopes`, which can be used with newly registered or updated clients. There are no whitelisted scopes by default; only the client scopes, which are defined as `Realm Default Client Scopes` are whitelisted by default.
- Full Scope Policy - Newly registered clients will have `Full Scope Allowed` switch disabled. This means they won’t have any scoped realm roles or client roles of other clients.
- Max Clients Policy - Rejects registration if current number of clients in the realm is same or bigger than specified limit. It’s 200 by default for anonymous registrations.
- Client Disabled Policy - Newly registered client will be disabled. This means that admin needs to manually approve and enable all newly registered clients. This policy is not used by default even for anonymous registration.

## 6. Client Registration CLI {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/client-registration/client-registration-cli.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/client-registration/client-registration-cli.adoc)

The Client Registration CLI is a command-line interface (CLI) tool for application developers to configure new clients in a self-service manner when integrating with Keycloak. It is specifically designed to interact with Keycloak Client Registration REST endpoints.

It is necessary to create or obtain a client configuration for any application to be able to use Keycloak. You usually configure a new client for each new application hosted on a unique host name. When an application interacts with Keycloak, the application identifies itself with a client ID so Keycloak can provide a login page, single sign-on (SSO) session management, and other services.

You can configure application clients from a command line with the Client Registration CLI, and you can use it in shell scripts.

To allow a particular user to use `Client Registration CLI` the Keycloak administrator typically uses the Admin Console to configure a new user with proper roles or to configure a new client and client secret to grant access to the Client Registration REST API.

### 6.1. Configuring a new regular user for use with Client Registration CLI {#}

1. Log in to the Admin Console (for example, <http://localhost:8080/auth/admin>) as `admin`.

2. Select a realm to administer.

3. If you want to use an existing user, select that user to edit; otherwise, create a new user.

4. Select **Role Mappings > Client Roles > realm-management**. If you are in the master realm, select **NAME-realm**, where `NAME` is the name of the target realm. You can grant access to any other realm to users in the master realm.

5. Select **Available Roles > manage-client** to grant a full set of client management permissions. Another option is to choose **view-clients** for read-only or **create-client** to create new clients.

   |      | These permissions grant the user the capability to perform operations without the use of [Initial Access Token](https://www.keycloak.org/docs/latest/securing_apps/index.html#_initial_access_token) or [Registration Access Token](https://www.keycloak.org/docs/latest/securing_apps/index.html#_registration_access_token). |
   | ---- | ------------------------------------------------------------ |
   |      |                                                              |

It is possible to not assign any `realm-management` roles to a user. In that case, a user can still log in with the Client Registration CLI but cannot use it without an Initial Access Token. Trying to perform any operations without a token results in a **403 Forbidden** error.

The Administrator can issue Initial Access Tokens from the Admin Console through the **Realm Settings > Client Registration > Initial Access Token** menu.

### 6.2. Configuring a client for use with the Client Registration CLI {#}

By default, the server recognizes the Client Registration CLI as the `admin-cli` client, which is configured automatically for every new realm. No additional client configuration is necessary when logging in with a user name.

1. Create a new client (for example, `reg-cli`) if you want to use a separate client configuration for the Client Registration CLI.
2. Toggle the **Standard Flow Enabled** setting it to **Off**.
3. Strengthen the security by configuring the client `Access Type` as `Confidential` and selecting **Credentials > ClientId and Secret**.

You can configure either `Client Id and Secret` or `Signed JWT` under the **Credentials** tab .

1. Enable service accounts if you want to use a service account associated with the client by selecting a client to edit in the **Clients** section of the `Admin Console`.
   1. Under **Settings**, change the **Access Type** to **Confidential**, toggle the **Service Accounts Enabled** setting to **On**, and click **Save**.
   2. Click **Service Account Roles** and select desired roles to configure the access for the service account. For the details on what roles to select, see [Configuring a new regular user for use with Client Registration CLI](https://www.keycloak.org/docs/latest/securing_apps/index.html#_configuring_a_user_for_client_registration_cli).
2. Toggle the **Direct Access Grants Enabled** setting it to **On** if you want to use a regular user account instead of a service account.
3. If the client is configured as `Confidential`, provide the configured secret when running `kcreg config credentials`by using the `--secret` option.
4. Specify which `clientId` to use (for example, `--client reg-cli`) when running `kcreg config credentials`.
5. With the service account enabled, you can omit specifying the user when running `kcreg config credentials` and only provide the client secret or keystore information.

### 6.3. Installing the Client Registration CLI {#}

The Client Registration CLI is packaged inside the Keycloak Server distribution. You can find execution scripts inside the `bin`directory. The Linux script is called `kcreg.sh`, and the Windows script is called `kcreg.bat`.

Add the Keycloak server directory to your `PATH` when setting up the client for use from any location on the file system.

For example, on:

- Linux:

```
$ export PATH=$PATH:$KEYCLOAK_HOME/bin
$ kcreg.sh
```

- Windows:

```
c:\> set PATH=%PATH%;%KEYCLOAK_HOME%\bin
c:\> kcreg
```

`KEYCLOAK_HOME` refers to a directory where the Keycloak Server distribution was unpacked.

### 6.4. Using the Client Registration CLI {#}

1. Start an authenticated session by logging in with your credentials.

2. Run commands on the `Client Registration REST` endpoint.

   For example, on:

   - Linux:

     ```
     $ kcreg.sh config credentials --server http://localhost:8080/auth --realm demo --user user --client reg-cli
     $ kcreg.sh create -s clientId=my_client -s 'redirectUris=["http://localhost:8980/myapp/*"]'
     $ kcreg.sh get my_client
     ```

   - Windows:

     ```
     c:\> kcreg config credentials --server http://localhost:8080/auth --realm demo --user user --client reg-cli
     c:\> kcreg create -s clientId=my_client -s "redirectUris=[\"http://localhost:8980/myapp/*\"]"
     c:\> kcreg get my_client
     ```

     |      | In a production environment, Keycloak has to be accessed with `https:` to avoid exposing tokens to network sniffers. |
     | ---- | ------------------------------------------------------------ |
     |      |                                                              |

3. If a server’s certificate is not issued by one of the trusted certificate authorities (CAs) that are included in Java’s default certificate truststore, prepare a `truststore.jks` file and instruct the Client Registration CLI to use it.

   For example, on:

   - Linux:

     ```
     $ kcreg.sh config truststore --trustpass $PASSWORD ~/.keycloak/truststore.jks
     ```

   - Windows:

     ```
     c:\> kcreg config truststore --trustpass %PASSWORD% %HOMEPATH%\.keycloak\truststore.jks
     ```

#### 6.4.1. Logging in {#}

1. Specify a server endpoint URL and a realm when you log in with the Client Registration CLI.
2. Specify a user name or a client id, which results in a special service account being used. When using a user name, you must use a password for the specified user. When using a client ID, you use a client secret or a `Signed JWT` instead of a password.

Regardless of the login method, the account that logs in needs proper permissions to be able to perform client registration operations. Keep in mind that any account in a non-master realm can only have permissions to manage clients within the same realm. If you need to manage different realms, you can either configure multiple users in different realms, or you can create a single user in the `master` realm and add roles for managing clients in different realms.

You cannot configure users with the Client Registration CLI. Use the Admin Console web interface or the Admin Client CLI to configure users. See [Server Administration Guide](https://www.keycloak.org/docs/6.0/server_admin/) for more details.

When `kcreg` successfully logs in, it receives authorization tokens and saves them in a private configuration file so the tokens can be used for subsequent invocations. See [Working with alternative configurations](https://www.keycloak.org/docs/latest/securing_apps/index.html#_working_with_alternative_configurations) for more information on configuration files.

See the built-in help for more information on using the Client Registration CLI.

For example, on:

- Linux:

```
$ kcreg.sh help
```

- Windows:

```
c:\> kcreg help
```

See `kcreg config credentials --help` for more information about starting an authenticated session.

#### 6.4.2. Working with alternative configurations {#}

By default, the Client Registration CLI automatically maintains a configuration file at a default location, `./.keycloak/kcreg.config`, under the user’s home directory. You can use the `--config` option to point to a different file or location to mantain multiple authenticated sessions in parallel. It is the safest way to perform operations tied to a single configuration file from a single thread.

|      | Do not make the configuration file visible to other users on the system. The configuration file contains access tokens and secrets that should be kept private. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

You might want to avoid storing secrets inside a configuration file by using the `--no-config` option with all of your commands, even though it is less convenient and requires more token requests to do so. Specify all authentication information with each `kcreg` invocation.

#### 6.4.3. Initial Access and Registration Access Tokens {#}

Developers who do not have an account configured at the Keycloak server they want to use can use the Client Registration CLI. This is possible only when the realm administrator issues a developer an Initial Access Token. It is up to the realm administrator to decide how and when to issue and distribute these tokens. The realm administrator can limit the maximum age of the Initial Access Token and the total number of clients that can be created with it.

Once a developer has an Initial Access Token, the developer can use it to create new clients without authenticating with `kcreg config credentials`. The Initial Access Token can be stored in the configuration file or specified as part of the `kcreg create` command.

For example, on:

- Linux:

```
$ kcreg.sh config initial-token $TOKEN
$ kcreg.sh create -s clientId=myclient
```

or

```
$ kcreg.sh create -s clientId=myclient -t $TOKEN
```

- Windows:

```
c:\> kcreg config initial-token %TOKEN%
c:\> kcreg create -s clientId=myclient
```

or

```
c:\> kcreg create -s clientId=myclient -t %TOKEN%
```

When using an Initial Access Token, the server response includes a newly issued Registration Access Token. Any subsequent operation for that client needs to be performed by authenticating with that token, which is only valid for that client.

The Client Registration CLI automatically uses its private configuration file to save and use this token with its associated client. As long as the same configuration file is used for all client operations, the developer does not need to authenticate to read, update, or delete a client that was created this way.

See [Client Registration](https://www.keycloak.org/docs/latest/securing_apps/index.html#_client_registration) for more information about Initial Access and Registration Access Tokens.

Run the `kcreg config initial-token --help` and `kcreg config registration-token --help` commands for more information on how to configure tokens with the Client Registration CLI.

#### 6.4.4. Creating a client configuration {#}

The first task after authenticating with credentials or configuring an Initial Access Token is usually to create a new client. Often you might want to use a prepared JSON file as a template and set or override some of the attributes.

The following example shows how to read a JSON file, override any client id it may contain, set any other attributes, and print the configuration to a standard output after successful creation.

- Linux:

```
$ kcreg.sh create -f client-template.json -s clientId=myclient -s baseUrl=/myclient -s 'redirectUris=["/myclient/*"]' -o
```

- Windows:

```
C:\> kcreg create -f client-template.json -s clientId=myclient -s baseUrl=/myclient -s "redirectUris=[\"/myclient/*\"]" -o
```

Run the `kcreg create --help` for more information about the `kcreg create` command.

You can use `kcreg attrs` to list available attributes. Keep in mind that many configuration attributes are not checked for validity or consistency. It is up to you to specify proper values. Remember that you should not have any id fields in your template and should not specify them as arguments to the `kcreg create` command.

#### 6.4.5. Retrieving a client configuration {#}

You can retrieve an existing client by using the `kcreg get` command.

For example, on:

- Linux:

```
$ kcreg.sh get myclient
```

- Windows:

```
C:\> kcreg get myclient
```

You can also retrieve the client configuration as an adapter configuration file, which you can package with your web application.

For example, on:

- Linux:

```
$ kcreg.sh get myclient -e install > keycloak.json
```

- Windows:

```
C:\> kcreg get myclient -e install > keycloak.json
```

Run the `kcreg get --help` command for more information about the `kcreg get` command.

#### 6.4.6. Modifying a client configuration {#}

There are two methods for updating a client configuration.

One method is to submit a complete new state to the server after getting the current configuration, saving it to a file, editing it, and posting it back to the server.

For example, on:

- Linux:

```
$ kcreg.sh get myclient > myclient.json
$ vi myclient.json
$ kcreg.sh update myclient -f myclient.json
```

- Windows:

```
C:\> kcreg get myclient > myclient.json
C:\> notepad myclient.json
C:\> kcreg update myclient -f myclient.json
```

The second method fetches the current client, sets or deletes fields on it, and posts it back in one step.

For example, on:

- Linux:

```
$ kcreg.sh update myclient -s enabled=false -d redirectUris
```

- Windows:

```
C:\> kcreg update myclient -s enabled=false -d redirectUris
```

You can also use a file that contains only changes to be applied so you do not have to specify too many values as arguments. In this case, specify `--merge` to tell the Client Registration CLI that rather than treating the JSON file as a full, new configuration, it should treat it as a set of attributes to be applied over the existing configuration.

For example, on:

- Linux:

```
$ kcreg.sh update myclient --merge -d redirectUris -f mychanges.json
```

- Windows:

```
C:\> kcreg update myclient --merge -d redirectUris -f mychanges.json
```

Run the `kcreg update --help` command for more information about the `kcreg update` command.

#### 6.4.7. Deleting a client configuration {#}

Use the following example to delete a client.

- Linux:

```
$ kcreg.sh delete myclient
```

- Windows:

```
C:\> kcreg delete myclient
```

Run the `kcreg delete --help` command for more information about the `kcreg delete` command.

#### 6.4.8. Refreshing invalid Registration Access Tokens {#}

When performing a create, read, update, and delete (CRUD) operation using the `--no-config` mode, the Client Registration CLI cannot handle Registration Access Tokens for you. In that case, it is possible to lose track of the most recently issued Registration Access Token for a client, which makes it impossible to perform any further CRUD operations on that client without authenticating with an account that has **manage-clients** permissions.

If you have permissions, you can issue a new Registration Access Token for the client and have it printed to a standard output or saved to a configuration file of your choice. Otherwise, you have to ask the realm administrator to issue a new Registration Access Token for your client and send it to you. You can then pass it to any CRUD command via the `--token` option. You can also use the `kcreg config registration-token` command to save the new token in a configuration file and have the Client Registration CLI automatically handle it for you from that point on.

Run the `kcreg update-token --help` command for more information about the `kcreg update-token` command.

### 6.5. Troubleshooting {#}

- Q: When logging in, I get an error: *Parameter client_assertion_type is missing [invalid_client].

  A: This error means your client is configured with `Signed JWT` token credentials, which means you have to use the `--keystore` parameter when logging in.

## 7. Token Exchange {#}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/token-exchange/token-exchange.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/token-exchange/token-exchange.adoc)

|      | Token Exchange is **Technology Preview** and is not fully supported. This feature is disabled by default.To enable start the server with `-Dkeycloak.profile=preview` or `-Dkeycloak.profile.feature.token_exchange=enabled` . For more details see [Profiles](https://www.keycloak.org/docs/6.0/server_installation/#profiles). |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

In Keycloak, token exchange is the process of using a set of credentials or token to obtain an entirely different token. A client may want to invoke on a less trusted application so it may want to downgrade the current token it has. A client may want to exchange a Keycloak token for a token stored for a linked social provider account. You may want to trust external tokens minted by other Keycloak realms or foreign IDPs. A client may have a need to impersonate a user. Here’s a short summary of the current capabilities of Keycloak around token exchange.

- A client can exchange an existing Keycloak token created for a specific client for a new token targeted to a different client
- A client can exchange an existing Keycloak token for an external token, i.e. a linked Facebook account
- A client can exchange an external token for a Keycloak token.
- A client can impersonate a user

Token exchange in Keycloak is a very loose implementation of the [OAuth Token Exchange](https://tools.ietf.org/html/draft-ietf-oauth-token-exchange-16) specification at the IETF. We have extended it a little, ignored some of it, and loosely interpreted other parts of the specification. It is a simple grant type invocation on a realm’s OpenID Connect token endpoint.

```
/auth/realms/{realm}/protocol/openid-connect/token
```

It accepts form parameters (`application/x-www-form-urlencoded`) as input and the output depends on the type of token you requested an exchange for. Token exchange is a client endpoint so requests must provide authentication information for the calling client. Public clients specify their client identifier as a form parameter. Confidential clients can also use form parameters to pass their client id and secret, Basic Auth, or however your admin has configured the client authentication flow in your realm. Here’s a list of form parameters

- client_id

  *REQUIRED MAYBE.* This parameter is required for clients using form parameters for authentication. If you are using Basic Auth, a client JWT token, or client cert authentication, then do not specify this parameter.

- client_secret

  *REQUIRED MAYBE*. This parameter is required for clients using form parameters for authentication and using a client secret as a credential. Do not specify this parameter if client invocations in your realm are authenticated by a different means.

- grant_type

  *REQUIRED.* The value of the parameter must be `urn:ietf:params:oauth:grant-type:token-exchange`.

- subject_token

  *OPTIONAL.* A security token that represents the identity of the party on behalf of whom the request is being made. It is required if you are exchanging an existing token for a new one.

- subject_issuer

  *OPTIONAL.* Identifies the issuer of the `subject_token`. It can be left blank if the token comes from the current realm or if the issuer can be determined from the `subject_token_type`. Otherwise it is required to be specified. Valid values are the alias of an `Identity Provider` configured for your realm. Or an issuer claim identifier configured by a specific `Identity Provider`.

- subject_token_type

  *OPTIONAL.* This parameter is the type of the token passed with the `subject_token` parameter. This defaults to `urn:ietf:params:oauth:token-type:access_token` if the `subject_token` comes from the realm and is an access token. If it is an external token, this parameter may or may not have to be specified depending on the requirements of the`subject_issuer`.

- requested_token_type

  *OPTIONAL.* This parameter represents the type of token the client wants to exchange for. Currently only oauth and OpenID Connect token types are supported. The default value for this depends on whether the is `urn:ietf:params:oauth:token-type:refresh_token` in which case you will be returned both an access token and refresh token within the response. Other appropriate values are `urn:ietf:params:oauth:token-type:access_token`and `urn:ietf:params:oauth:token-type:id_token`

- audience

  *OPTIONAL.* This parameter specifies the target client you want the new token minted for.

- requested_issuer

  *OPTIONAL.* This parameter specifies that the client wants a token minted by an external provider. It must be the alias of an `Identity Provider` configured within the realm.

- requested_subject

  *OPTIONAL.* This specifies a username or user id if your client wants to impersonate a different user.

- scope

  *NOT IMPLEMENTED.* This parameter represents the target set of OAuth and OpenID Connect scopes the client is requesting. It is not implemented at this time but will be once Keycloak has better support for scopes in general.

|      | We currently only support OpenID Connect and OAuth exchanges. Support for SAML based clients and identity providers may be added in the future depending on user demand. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

A successful response from an exchange invocation will return the HTTP 200 response code with a content type that depends on the `requested-token-type` and `requested_issuer` the client asks for. OAuth requested token types will return a JSON document as described in the [OAuth Token Exchange](https://tools.ietf.org/html/draft-ietf-oauth-token-exchange-16) specification.

```
{
   "access_token" : ".....",
   "refresh_token" : ".....",
   "expires_in" : "...."
 }
```

Clients requesting a refresh token will get back both an access and refresh token in the response. Clients requesting only access token type will only get an access token in the response. Expiration information may or may not be included for clients requesting an external issuer through the `requested_issuer` paramater.

Error responses generally fall under the 400 HTTP response code category, but other error status codes may be returned depending on the severity of the error. Error responses may include content depending on the `requested_issuer`. OAuth based exchanges may return a JSON document as follows:

```
{
   "error" : "...."
   "error_description" : "...."
}
```

Additional error claims may be returned depending on the exchange type. For example, OAuth Identity Providers may include an additional `account-link-url` claim if the user does not have a link to an identity provider. This link can be used for a client initiated link request.

|      | Token exchange setup requires knowledge of fine grain admin permissions (See the [Server Administration Guide](https://www.keycloak.org/docs/6.0/server_admin/) for more information). You will need to grant clients permission to exchange. This is discussed more later in this chapter. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

The rest of this chapter discusses the setup requirements and provides examples for different exchange scenarios. For simplicity’s sake, let’s call a token minted by the current realm as an *internal* token and a token minted by an external realm or identity provider as an *external* token.

### 7.1. Internal Token to Internal Token Exchange {#}

With an internal token to token exchange you have an existing token minted to a specific client and you want to exchange this token for a new one minted for a different target client. Why would you want to do this? This generally happens when a client has a token minted for itself, and needs to make additional requests to other applications that require different claims and permissions within the access token. Other reasons this type of exchange might be required is if you need to perform a "permission downgrade" where your app needs to invoke on a less trusted app and you don’t want to propagate your current access token.

#### 7.1.1. Granting Permission for the Exchange {#}

Clients that want to exchange tokens for a different client need to be authorized in the admin console to do so. You’ll need to define a `token-exchange` fine grain permission in the target client you want permission to exchange to.

Target Client Permission

![exchange target client permission unset](https://www.keycloak.org/docs/latest/securing_apps/keycloak-images/exchange-target-client-permission-unset.png)

Toggle the `Permissions Enabled` switch to ON.

Target Client Permission

![exchange target client permission set](https://www.keycloak.org/docs/latest/securing_apps/keycloak-images/exchange-target-client-permission-set.png)

You should see a `token-exchange` link on the page. Click that to start defining the permission. It will bring you to this page.

Target Client Exchange Permission Setup

![exchange target client permission setup](https://www.keycloak.org/docs/latest/securing_apps/keycloak-images/exchange-target-client-permission-setup.png)

You’ll have to define a policy for this permission. Click the `Authorization` link, go to the `Policies` tab and create a `Client` Policy.

Client Policy Creation

![exchange target client policy](https://www.keycloak.org/docs/latest/securing_apps/keycloak-images/exchange-target-client-policy.png)

Here you enter in the starting client, that is the authenticated client that is requesting a token exchange. After you create this policy, go back to the target client’s `token-exchange` permission and add the client policy you just defined.

Apply Client Policy

![exchange target client exchange apply policy](https://www.keycloak.org/docs/latest/securing_apps/keycloak-images/exchange-target-client-exchange-apply-policy.png)

Your client now has permission to invoke. If you do not do this correctly, you will get a 403 Forbidden response if you try to make an exchange.

#### 7.1.2. Making the Request {#}

When your client is exchanging an existing token for a token targeting another client, you must use the `audience`parameter. This parameter must be the client identifier for the target client that you configured in the admin console.

```
curl -X POST \
    -d "client_id=starting-client" \
    -d "client_secret=geheim" \
    --data-urlencode "grant_type=urn:ietf:params:oauth:grant-type:token-exchange" \
    -d "subject_token=...." \
    --data-urlencode "requested_token_type=urn:ietf:params:oauth:token-type:refresh_token" \
    -d "audience=target-client" \
    http://localhost:8080/auth/realms/myrealm/protocol/openid-connect/token
```

The `subject_token` parameter must be an access token for the target realm. If your `requested_token_type` parameter is a refresh token type, then the response will contain both an access token, refresh token, and expiration. Here’s an example JSON response you get back from this call.

```
{
   "access_token" : "....",
   "refresh_token" : "....",
   "expires_in" : 3600
}
```

### 7.2. Internal Token to External Token Exchange {#}

You can exchange a realm token for an externl token minted by an external identity provider. This external identity provider must be configured within the `Identity Provider` section of the admin console. Currently only OAuth/OpenID Connect based external identity providers are supported, this includes all social providers. Keycloak does not perform a backchannel exchange to the external provider. So if the account is not linked, you will not be able to get the external token. To be able to obtain an external token one of these conditions must be met:

- The user must have logged in with the external identity provider at least once
- The user must have linked with the external identity provider through the User Account Service
- The user account was linked through the external identity provider using [Client Initiated Account Linking](https://www.keycloak.org/docs/6.0/server_development/) API.

Finally, the external identity provider must have been configured to store tokens, or, one of the above actions must have been performed with the same user session as the internal token you are exchanging.

If the account is not linked, the exchange response will contain a link you can use to establish it. This is discussed more in the [Making the Request](https://www.keycloak.org/docs/latest/securing_apps/index.html#_internal_external_making_request) section.

#### 7.2.1. Granting Permission for the Exchange {#}

Internal to external token exchange requests will be denied with a 403, Forbidden response until you grant permission for the calling client to exchange tokens with the external identity provider. To grant permission to the client you must go to the identity provider’s configuration page to the `Permissions` tab.

Identity Provider Permission

![exchange idp permission unset](assets/exchange-idp-permission-unset.png)

Toggle the `Permissions Enabled` switch to true.

Identity Provider Permission

![exchange idp permission set](assets/exchange-idp-permission-set.png)

You should see a `token-exchange` link on the page. Click that to start defining the permission. It will bring you to this page.

Identity Provider Exchange Permission Setup

![exchange idp permission setup](assets/exchange-idp-permission-setup.png)

You’ll have to define a policy for this permission. Click the `Authorization` link, go to the `Policies` tab and create a `Client` Policy.

Client Policy Creation

![exchange idp client policy](assets/exchange-idp-client-policy.png)

Here you enter in the starting client, that is the authenticated client that is requesting a token exchange. After you create this policy, go back to the identity providers’s `token-exchange` permission and add the client policy you just defined.

Apply Client Policy

![exchange idp apply policy](assets/exchange-idp-apply-policy.png)

Your client now has permission to invoke. If you do not do this correctly, you will get a 403 Forbidden response if you try to make an exchange.

#### 7.2.2. Making the Request {#}

When your client is exchanging an existing internal token to an external one, you must provide the `requested_issuer`parameter. The parameter must be the alias of a configured identity provider.

```
curl -X POST \
    -d "client_id=starting-client" \
    -d "client_secret=geheim" \
    --data-urlencode "grant_type=urn:ietf:params:oauth:grant-type:token-exchange" \
    -d "subject_token=...." \
    --data-urlencode "requested_token_type=urn:ietf:params:oauth:token-type:access_token" \
    -d "requested_issuer=google" \
    http://localhost:8080/auth/realms/myrealm/protocol/openid-connect/token
```

The `subject_token` parameter must be an access token for the target realm. The `requested_token_type` parameter must be `urn:ietf:params:oauth:token-type:access_token` or left blank. No other requested token type is supported at this time. Here’s an example successful JSON response you get back from this call.

```
{
   "access_token" : "....",
   "expires_in" : 3600
   "account-link-url" : "https://...."
}
```

If the external identity provider is not linked for whatever reason, you will get an HTTP 400 response code with this JSON document:

```
{
   "error" : "....",
   "error_description" : "..."
   "account-link-url" : "https://...."
}
```

The `error` claim will be either `token_expired` or `not_linked`. The `account-link-url` claim is provided so that the client can perform [Client Initiated Account Linking](https://www.keycloak.org/docs/6.0/server_development/). Most (all?) providers are requiring linking through browser OAuth protocol. With the `account-link-url` just add a `redirect_uri` query parameter to it and you can forward browsers to perform the link.

### 7.3. External Token to Internal Token Exchange {#}

You can trust and exchange external tokens minted by external identity providers for internal tokens. This can be used to bridge between realms or just to trust tokens from your social provider. It works similarly to an identity provider browser login in that a new user is imported into your realm if it doesn’t exist.

|      | The current limitation on external token exchanges is that if the external token maps to an existing user an exchange will not be allowed unless the existing user already has an account link to the external identity provider. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

When the exchange is complete, a user session will be created within the realm, and you will receive an access and or refresh token depending on the `requested_token_type` parameter value. You should note that this new user session will remain active until it times out or until you call the logout endpoint of the realm passing this new access token.

These types of changes required a configured identity provider in the admin console.

|      | SAML identity providers are not supported at this time. Twitter tokens cannot be exchanged either. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

#### 7.3.1. Granting Permission for the Exchange {#}

Before external token exchanges can be done, you must grant permission for the calling client to make the exchange. This permission is granted in the same manner as [internal to external permission is granted](https://www.keycloak.org/docs/latest/securing_apps/index.html#_grant_permission_external_exchange).

If you also provide an `audience` parameter whose value points to a different client other than the calling one, you must also grant the calling client permission to exchange to the target client specific in the `audience` parameter. How to do this is [discussed earlier](https://www.keycloak.org/docs/latest/securing_apps/index.html#_client_to_client_permission) in this section.

#### 7.3.2. Making the Request {#}

The `subject_token_type` must either be `urn:ietf:params:oauth:token-type:access_token` or `urn:ietf:params:oauth:token-type:jwt`. If the type is `urn:ietf:params:oauth:token-type:access_token` you must specify the `subject_issuer` parameter and it must be the alias of the configured identity provider. If the type is `urn:ietf:params:oauth:token-type:jwt`, the provider will be matched via the `issuer` claim within the JWT which must be the alias of the provider, or a registered issuer within the providers configuration.

For validation, if the token is an access token, the provider’s user info service will be invoked to validate the token. A successful call will mean that the access token is valid. If the subject token is a JWT and if the provider has signature validation enabled, that will be attempted, otherwise, it will default to also invoking on the user info service to validate the token.

By default, the internal token minted will use the calling client to determine what’s in the token using the protocol mappers defined for the calling client. Alternatively, you can specify a different target client using the `audience` parameter.

```
curl -X POST \
    -d "client_id=starting-client" \
    -d "client_secret=geheim" \
    --data-urlencode "grant_type=urn:ietf:params:oauth:grant-type:token-exchange" \
    -d "subject_token=...." \
    -d "subject_issuer=myOidcProvider" \
    --data-urlencode "subject_token_type=urn:ietf:params:oauth:token-type:access_token" \
    -d "audience=target-client" \
    http://localhost:8080/auth/realms/myrealm/protocol/openid-connect/token
```

If your `requested_token_type` parameter is a refresh token type, then the response will contain both an access token, refresh token, and expiration. Here’s an example JSON response you get back from this call.

```
{
   "access_token" : "....",
   "refresh_token" : "....",
   "expires_in" : 3600
}
```

### 7.4. Impersonation {#}

For internal and external token exchanges, the client can request on behalf of a user to impersonate a different user. For example, you may have an admin application that needs to impersonate a user so that a support engineer can debug a problem.

#### 7.4.1. Granting Permission for the Exchange {#}

The user that the subject token represents must have permission to impersonate other users. See the [Server Administration Guide](https://www.keycloak.org/docs/6.0/server_admin/) on how to enable this permission. It can be done through a role or through fine grain admin permissions.

#### 7.4.2. Making the Request {#}

Make the request as described in other chapters except additionally specify the `request_subject` parameter. The value of this parameter must be a username or user id.

```
curl -X POST \
    -d "client_id=starting-client" \
    -d "client_secret=geheim" \
    --data-urlencode "grant_type=urn:ietf:params:oauth:grant-type:token-exchange" \
    -d "subject_token=...." \
    --data-urlencode "requested_token_type=urn:ietf:params:oauth:token-type:access_token" \
    -d "audience=target-client" \
    -d "requested_subject=wburke" \
    http://localhost:8080/auth/realms/myrealm/protocol/openid-connect/token
```

### 7.5. Direct Naked Impersonation {#}

You can make an internal token exchange request without providing a `subject_token`. This is called a direct naked impersonation because it places a lot of trust in a client as that client can impersonate any user in the realm. You might need this to bridge for applications where it is impossible to obtain a subject token to exchange. For example, you may be integrating a legacy application that performs login directly with LDAP. In that case, the legacy app is able to authenticate users itself, but not able to obtain a token.

|      | It is very risky to enable direct naked impersonation for a client. If the client’s credentials are ever stolen, that client can impersonate any user in the system. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

#### 7.5.1. Granting Permission for the Exchange {#}

If the `audience` parameter is provided, then the calling client must have permission to exchange to the client. How to set this up is discussed earlier in this chapter.

Additionally, the calling client must be granted permission to impersonate users. In the admin console, go to the `Users`screen and click on the `Permissions` tab.

Users Permission

![exchange users permission unset](assets/exchange-users-permission-unset.png)

Toggle the `Permissions Enabled` switch to true.

Identity Provider Permission

![exchange users permission set](assets/exchange-users-permission-set.png)

You should see a `impersonation` link on the page. Click that to start defining the permission. It will bring you to this page.

Users Impersonation Permission Setup

![exchange users permission setup](assets/exchange-users-permission-setup.png)

You’ll have to define a policy for this permission. Click the `Authorization` link, go to the `Policies` tab and create a `Client` Policy.

Client Policy Creation

![exchange users client policy](assets/exchange-users-client-policy.png)

Here you enter in the starting client, that is the authenticated client that is requesting a token exchange. After you create this policy, go back to the users' `impersonation` permission and add the client policy you just defined.

Apply Client Policy

![exchange users apply policy](assets/exchange-users-apply-policy.png)

Your client now has permission to impersonate users. If you do not do this correctly, you will get a 403 Forbidden response if you try to make this type of exchange.

|      | Public clients are not allowed to do direct naked impersonations. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

#### 7.5.2. Making the Request {#}

To make the request, simply specify the `requested_subject` parameter. This must be the username or user id of a valid user. You can also specify an `audience` parameter if you wish.

```
curl -X POST \
    -d "client_id=starting-client" \
    -d "client_secret=geheim" \
    --data-urlencode "grant_type=urn:ietf:params:oauth:grant-type:token-exchange" \
    -d "requested_subject=wburke" \
    http://localhost:8080/auth/realms/myrealm/protocol/openid-connect/token
```

### 7.6. Expand Permission Model With Service Accounts {#}

When granting clients permission to exchange, you don’t necessarily have to manually enable those permissions for each and every client. If the client has a service account associated with it, you can use a role to group permissions together and assign exchange permissions by assigning a role to the client’s service account. For example, you might define a `naked-exchange` role and any service account that has that role can do a naked exchange.

### 7.7. Exchange Vulnerabilities {#}

When you start allowing token exchanges, there’s various things you have to both be aware of and careful of.

The first is public clients. Public clients do not have or require a client credential in order to perform an exchange. Anybody that has a valid token will be able to *impersonate* the public client and perform the exchanges that public client is allowed to perform. If there are any untrustworthy clients that are managed by your realm, public clients may open up vulnerabilities in your permission models. This is why direct naked exchanges do not allow public clients and will abort with an error if the calling client is public.

It is possible to exchange social tokens provided by Facebook, Google, etc. for a realm token. Be careful and vigilante on what the exchange token is allowed to do as its not hard to create fake accounts on these social websites. Use default roles, groups, and identity provider mappers to control what attributes and roles are assigned to the external social user.

Direct naked exchanges are quite dangerous. You are putting a lot of trust in the calling client that it will never leak out its client credentials. If those credentials are leaked, then the thief can impersonate anybody in your system. This is in direct contrast to confidential clients that have existing tokens. You have two factors of authentication, the access token and the client credentials, and you’re only dealing with one user. So use direct naked exchanges sparingly.


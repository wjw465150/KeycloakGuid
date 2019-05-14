# 保护应用程序和服务指南 {#Securing_Applications_and_Services_Guide}



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

#### 1.3.1. OpenID Connect {#OpenID_Connect}

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

### 2.1. Java 适配器 {#Java_Adapters}

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

##### JBoss SSO {#JBoss_SSO}
WildFly内置支持部署到同一WildFly实例的Web应用程序的单点登录。 使用Keycloak时不应启用此功能。

##### 每个WAR配置必需 {#WAR}
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

##### 通过适配器子系统保护WAR {#WAR}
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

###### 保护JBoss EAP 6.4上的Hawtio {#Securing_Hawtio_on_JBoss_EAP_6_4}
要在JBoss EAP 6.4服务器上运行Hawtio，请完成以下步骤:

1. 按照上一节“保护Hawtio管理控制台”中的说明设置Keycloak。 假设：

   - 你有一个Keycloak领域`demo`和客户`hawtio-client`
   - 你的Keycloak在`localhost:8080`上运行
   - 部署了Hawtio的JBoss EAP 6.4服务器将在`localhost:8181`上运行。 具有此服务器的目录在后续步骤中称为`$EAP_HOME`。

2. 将`hawtio-wildfly-1.4.0.redhat-630254.war`存档复制到`$EAP_HOME/standalone/configuration`目录。 有关部署Hawtio的更多详细信息，请参阅[Fuse Hawtio文档](https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.3/html-single/deploying_into_a_web_server/)。

3. 将带有上述内容的`keycloak-hawtio.json`和`keycloak-hawtio-client.json`文件复制到`$EAP_HOME/standalone/configuration`目录。

4. 按照[JBoss适配器文档](https://www.keycloak.org/docs/latest/securing_apps/index.html#_jboss_adapter)中的说明，将Keycloak适配器子系统安装到JBoss EAP 6.4服务器上。

5. 在`$EAP_HOME/standalone/configuration/standalone.xml`文件中配置系统属性，如下例所示：

   ```xml
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

6. 将Hawtio域添加到`security-domains`部分中的同一文件：

   ```xml
   <security-domain name="hawtio" cache-type="default">
       <authentication>
           <login-module code="org.keycloak.adapters.jaas.BearerTokenLoginModule" flag="required">
               <module-option name="keycloak-config-file" value="${hawtio.keycloakServerConfig}"/>
           </login-module>
       </authentication>
   </security-domain>
   ```

7. 将`secure-deployment`部分`hawtio`添加到适配器子系统。 这可确保Hawtio WAR能够找到JAAS登录模块类。

   ```xml
   <subsystem xmlns="urn:jboss:domain:keycloak:1.1">
       <secure-deployment name="hawtio-wildfly-1.4.0.redhat-630254.war" />
   </subsystem>
   ```

8. 使用Hawtio重新启动JBoss EAP 6.4服务器：

   ```bash
   cd $EAP_HOME/bin
   ./standalone.sh -Djboss.socket.binding.port-offset=101
   ```

9. 在<http://localhost:8181/hawtio>访问Hawtio。 它由Keycloak保护。

#### 2.1.5. JBoss Fuse 7 适配器 {#JBoss_Fuse_7_Adapter}

Keycloak支持保护在[JBoss Fuse 7](https://developers.redhat.com/products/fuse/overview/)中运行的Web应用程序。

JBoss Fuse 7利用了Undertow适配器，它与[EAP 7 / WildFly适配器](https://www.keycloak.org/docs/latest/securing_apps/index.html#_jboss_adapter)基本相同，因为捆绑了JBoss Fuse 7.2.0 使用[Undertow HTTP引擎](http://undertow.io/)，Undertow用于运行各种Web应用程序。

> 唯一受支持的Fuse 7版本是最新版本。 如果使用早期版本的Fuse 7，则某些功能可能无法正常工作。 特别是，对于低于7.0.1的Fuse 7版本，集成将完全不起作用。

Fuse支持以下项目的安全性:

- 使用Pax Web War Extender部署在Fuse上的经典WAR应用程序
- 使用Pax Web Whiteboard Extender作为OSGI服务部署在Fuse上的Servlet以及通过org.osgi.service.http.HttpService#registerServlet()注册的servlet，这是标准的OSGi Enterprise HTTP服务
- [Apache Camel](http://camel.apache.org/) 使用[Camel Undertow](http://camel.apache.org/undertow.html)组件运行的端点
- [Apache CXF](http://cxf.apache.org/) 端点在他们自己独立的Undertow引擎上运行
- [Apache CXF](http://cxf.apache.org/) 在CXF servlet提供的默认引擎上运行的端点
- SSH和JMX管理员访问权限
- [Hawtio管理控制台](https://hawt.io/)

##### 在Fuse 7中保护Web应用程序 {#Securing_Your_Web_Applications_Inside_Fuse_7}
您必须先安装Keycloak Karaf功能。 接下来，您需要根据要保护的应用程序类型执行这些步骤。 所有引用的Web应用程序都需要将Keycloak Undertow身份验证机制注入底层Web服务器。 实现此目标的步骤取决于应用程序类型。 细节描述如下。

最好的起点是看看作为`fuse`目录中Keycloak示例的一部分捆绑的Fuse演示。 通过测试和理解演示，大多数步骤都应该是可以理解的。

##### 安装Keycloak功能 {#Installing_the_Keycloak_Feature}

您必须首先在JBoss Fuse环境中安装`keycloak-pax-http-afow`和`keycloak-jaas`功能。 `keycloak-pax-http-undertow`功能包括Fuse适配器和所有第三方依赖项。 `keycloak-jaas`包含用于SSH和JMX身份验证的领域中的JAAS模块。 您可以从Maven存储库或存档中安装它。

###### 从Maven存储库安装 {#Installing_from_the_Maven_Repository}
作为先决条件，您必须在线并且可以访问Maven存储库。

对于社区来说，只要在maven中央存储库中提供所有工件和第三方依赖项就足够了。

要使用Maven存储库安装keycloak功能，请完成以下步骤：

1. 启动JBoss Fuse 7.2.0; 然后在Karaf终端类型：

   ```
   feature:repo-add mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
   feature:install keycloak-pax-http-undertow keycloak-jaas
   ```

2. 您可能还需要安装Undertow功能：

   ```
   feature:install pax-http-undertow
   ```

3. 确保已安装功能：

```
feature:list | grep keycloak
```

###### 从ZIP捆绑包安装 {#Installing_from_the_ZIP_bundle}
如果您处于脱机状态或不想使用Maven获取JAR文件和其他工件，这将非常有用。

要从ZIP存档安装Fuse适配器，请完成以下步骤：

1. 下载Keycloak Fuse适配器ZIP存档。

2. 将其解压缩到JBoss Fuse的根目录中。 然后将依赖项安装在`system`目录下。 您可以覆盖所有现有的jar文件。

   用于JBoss Fuse 7.2.0：

   ```bash
   cd /path-to-fuse/fuse-karaf-7.z
   unzip -q /path-to-adapter-zip/keycloak-fuse-adapter-6.0.1.zip
   ```

3. 启动Fuse并在Fuse /karaf终端中运行以下命令:

   ```
   feature:repo-add mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
   feature:install keycloak-pax-http-undertow keycloak-jaas
   ```

4. 安装相应的Undertow适配器。 由于工件可直接在JBoss Fuse`system`目录中使用，因此您无需使用Maven存储库。

##### 保护经典WAR应用程序 {#Securing_a_Classic_WAR_Application}

保护WAR应用程序所需的步骤如下：

1. 在`/WEB-INF/web.xml`文件中，声明必要的：

   - <security-constraint>元素中的安全性约束

   - <login-config>元素中的登录配置。 确保`<auth-method>`是`KEYCLOAK`。

   - <security-role>元素中的安全角色

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

2. 在WAR的`/WEB-INF/`目录中，创建一个新文件keycloak.json。 该配置文件的格式在[Java Adapters Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_java_adapter_config)部分中进行了描述。 也可以按[配置外部适配器](https://www.keycloak.org/docs/latest/securing_apps/index.html#config_external_adapter)中的说明在外部使用此文件。

   例如：

   ```json
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

3. 与Fuse 6适配器相反，MANIFEST.MF中不需要特殊的OSGi导入。

###### 配置解析器 {#Configuration_Resolvers}
`keycloak.json`适配器配置文件可以存储在捆绑包中，这是默认行为，也可以存储在文件系统的目录中。 要指定配置文件的实际源，请将`keycloak.config.resolver`部署参数设置为所需的配置解析程序类。 例如，在经典的WAR应用程序中，在`web.xml`文件中设置`keycloak.config.resolver`上下文参数，如下所示：

```xml
<context-param>
    <param-name>keycloak.config.resolver</param-name>
    <param-value>org.keycloak.adapters.osgi.PathBasedKeycloakConfigResolver</param-value>
</context-param>
```

以下解析器可用于`keycloak.config.resolver`：

- org.keycloak.adapters.osgi.BundleBasedKeycloakConfigResolver

  这是默认的解析器。 预期配置文件位于受保护的OSGi包中。 默认情况下，它加载名为`WEB-INF/keycloak.json`的文件，但可以通过`configLocation`属性配置此文件名。

- org.keycloak.adapters.osgi.PathBasedKeycloakConfigResolver

  此解析程序在`keycloak.config`系统属性指定的文件夹中搜索名为`<your_web_context>-keycloak.json`的文件。 如果未设置`keycloak.config`，则使用`karaf.etc`系统属性。例如，如果您的Web应用程序部署到上下文`my-portal`中，那么您的适配器配置将从`${keycloak.config}/my-portal-keycloak.json`文件加载，或来自`${karaf.etc}/my-portal-keycloak.json`。

- org.keycloak.adapters.osgi.HierarchicalPathBasedKeycloakConfigResolver

  这个解析器类似于上面的`PathBasedKeycloakConfigResolver`，对于给定的URI路径，配置位置会从最特定到最不特定地进行检查。例如，对于`/my/web-app/context`URI，将搜索以下配置位置，直到第一个配置位置存在:`${karaf.etc}/my-web-app-context-keycloak.json``${karaf.etc}/my-web-app-keycloak.json``${karaf.etc}/my-keycloak.json``${karaf.etc}/keycloak.json`

##### 保护部署为OSGI服务的Servlet {#Securing_a_Servlet_Deployed_as_an_OSGI_Service}

如果在OSGI捆绑项目中有一个未部署为经典WAR应用程序的servlet类，则可以使用此方法。 Fuse使用Pax Web Whiteboard Extender将这些servlet部署为Web应用程序。

要使用Keycloak保护您的servlet，请完成以下步骤：

1. Keycloak提供了`org.keycloak.adapters.osgi.undertow.PaxWebIntegrationService`，它允许为您的应用程序配置身份验证方法和安全性约束。 您需要在应用程序内的`OSGI-INF/blueprint/blueprint.xml`文件中声明此类服务。 请注意，您的servlet需要依赖它。 配置示例：

   ```xml
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

   - 您可能需要在项目中包含`WEB-INF`目录（即使您的项目不是Web应用程序）并创建`/WEB-INF/keycloak.json`文件，如[Classic WAR application](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_adapter_classic_war)中所述部分。 请注意，您不需要`web.xml`文件，因为在蓝图配置文件中声明了安全性约束。

2. 与Fuse 6适配器相反，MANIFEST.MF中不需要特殊的OSGi导入。

##### 保护Apache Camel应用程序 {#Securing_an_Apache_Camel_Application}

您可以通过蓝图注入适当的安全约束并将使用过的组件更新为`undertow-keycloak`来保护使用[camel-undertow](http://camel.apache.org/undertow.html)组件实现的Apache Camel端点。 您必须使用类似以下配置将`OSGI-INF/blueprint/blueprint.xml`文件添加到Camel应用程序中。 角色和安全性约束映射以及适配器配置可能略有不同，具体取决于您的环境和需求。

与标准的`undertow`组件相比，`undertow-keycloak`组件增加了两个新属性：

- `configResolver`是一个解析器bean，提供Keycloak适配器配置。 可用的解析器列在[配置解析器](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_config_external_adapter)部分中。
- `allowedRoles`是以逗号分隔的角色列表。 访问服务的用户必须至少具有一个允许访问的角色。

例如：

```xml
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

- `META-INF/MANIFEST.MF`中的`Import-Package`需要包含这些导入：

```properties
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

##### Camel RestDSL {#Camel_RestDSL}
Camel Rest DSL是一种Camel功能，用于以流畅的方式定义REST端点。 但您仍必须使用特定的实现类，并提供有关如何与Keycloak集成的说明。

配置集成机制的方法取决于您为其配置RestDSL定义的路由的Camel组件。

以下示例显示如何使用`undertow-keycloak`组件配置集成，并引用前一个Blueprint示例中定义的一些bean。

```xml
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

##### 在单独的Undertow引擎上保护Apache CXF端点 {#Securing_an_Apache_CXF_Endpoint_on_a_Separate_Undertow_Engine}

要在单独的Undertow引擎上运行Keycloak保护的CXF端点，请完成以下步骤：

1. 将`OSGI-INF/blueprint/blueprint.xml`添加到您的应用程序中，并在其中添加与[Camel配置](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_adapter_camel)类似的正确配置解析程序bean。 在`httpu:engine-factory`中使用该camel配置声明`org.keycloak.adapters.osgi.undertow.CxfKeycloakAuthHandler`处理程序。 CFX JAX-WS应用程序的配置可能类似于以下内容：

   ```xml
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

2. `META-INF/MANIFEST.MF`中的`Import-Package`必须包含那些导入：

```properties
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

##### 在默认的Undertow引擎上保护Apache CXF端点 {#Securing_an_Apache_CXF_Endpoint_on_the_Default_Undertow_Engine}

某些服务会在启动时自动附带已部署的servlet。 一个这样的服务是在http:// localhost:8181/cxf上下文中运行的CXF servlet。 Fuse的Pax Web支持通过配置管理改变现有的上下文。 这可用于通过Keycloak保护端点。

应用程序中的配置文件`OSGI-INF/blueprint/blueprint.xml`可能类似于下面的那个。 请注意，它添加了JAX-RS `customerservice`端点，该端点是特定于端点的应用程序。

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
</blueprint>
```

此外，您必须创建`${karaf.etc}/org.ops4j.pax.web.context-*anyName*.cfg file`。 它将被视为工厂PID配置，由`pax-web-runtime`包跟踪。 此类配置可能包含以下属性，这些属性对应于标准`web.xml`的某些属性：

```properties
bundle.symbolicName = org.apache.cxf.cxf-rt-transports-http
context.id = default

context.param.keycloak.config.resolver = org.keycloak.adapters.osgi.HierarchicalPathBasedKeycloakConfigResolver

login.config.authMethod = KEYCLOAK

security.cxf.url = /cxf/customerservice/*
security.cxf.roles = admin, user
```

有关配置管理文件中可用属性的完整说明，请参阅Fuse文档。 上述属性具有以下含义：

- `bundle.symbolicName` 和 `context.id`

  在`org.ops4j.pax.web.service.WebContainer`中标识bundle及其部署上下文。

- `context.param.keycloak.config.resolver`

  为bundle提供`keycloak.config.resolver`上下文参数的值，与经典WAR中的`web.xml`相同。 可用的解析器在[配置解析器](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_config_external_adapter)部分中进行了描述。

- `login.config.authMethod`

  身份验证方法。 必须是`KEYCLOAK`。

- `security.*anyName*.url` 和 `security.*anyName*.roles`

  各个安全约束的属性值就像在`web.xml`中的`security-constraint/web-resource-collection/url-pattern` 和 `security-constraint/auth-constraint/role-name`中设置的那样， 分别。 角色由逗号和周围的空格分隔。 `* anyName*`标识符可以是任意的，但必须匹配相同安全约束的各个属性。一些Fuse版本包含一个错误，需要将角色分隔为`", "`（逗号和单个空格）。 确保使用这种表示法来分离角色。

`META-INF/MANIFEST.MF`中的`Import-Package`必须至少包含以下导入：

```properties
javax.ws.rs;version="[2,3)",
META-INF.cxf;version="[2.7,3.3)",
META-INF.cxf.osgi;version="[2.7,3.3)";resolution:=optional,
org.apache.cxf.transport.http;version="[2.7,3.3)",
org.apache.cxf.*;version="[2.7,3.3)",
com.fasterxml.jackson.jaxrs.json;version="${jackson.version}"
```

##### 保护Fuse管理服务 {#Securing_Fuse_Administration_Services}

###### 使用SSH身份验证来Fuse终端 {#Using_SSH_Authentication_to_Fuse_Terminal}
Keycloak主要处理用于Web应用程序身份验证的用例; 但是，如果您的其他Web服务和应用程序受Keycloak保护，则使用Keycloak凭据保护非Web管理服务（如SSH）是最佳实践。 您可以使用JAAS登录模块执行此操作，该模块允许远程连接到Keycloak并根据[资源所有者密码凭据](https://www.keycloak.org/docs/latest/securing_apps/index.html#_resource_owner_password_credentials_flow)验证凭据。

要启用SSH身份验证，请完成以下步骤：

1. 在Keycloak中创建一个客户端（例如，`ssh-jmx-admin-client`），它将用于SSH身份验证。 此客户端需要将`Direct Access Grants Enabled`选为`On`。

2. 在`$FUSE_HOME/etc/org.apache.karaf.shell.cfg`文件中，更新或指定此属性：

   ```properties
   sshRealm=keycloak
   ```

3. 添加`$FUSE_HOME/etc/ keycloak-direct-access.json`文件，其内容类似于以下内容（基于您的环境和Keycloak客户端设置）：

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

4. 启动Fuse并安装`keycloak` JAAS领域。 最简单的方法是安装`keycloak-jaas`功能，它具有预定义的JAAS领域。 您可以使用自己的`keycloak` JAAS领域覆盖该功能的预定义领域。 有关详细信息，请参阅[JBoss Fuse文档](https://access.redhat.com/documentation/en-us/red_hat_fuse/7.2/html-single/apache_karaf_security_guide/index#ESBSecureContainer)。

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

> 在某些更高版本的操作系统上，您可能还需要使用SSH命令的-o选项`-o HostKeyAlgorithms=+ssh-dss`，因为以后的SSH客户端默认情况下不允许使用`ssh-dss`算法。 但是，默认情况下，它目前在JBoss Fuse 7.2.0中使用。

请注意，用户需要具有领域角色`admin`来执行所有操作或其他角色来执行操作子集（例如，**viewer**角色限制用户仅运行只读的Karaf命令）。 可用角色在`$FUSE_HOME/etc/ org.apache.karaf.shell.cfg`或`$FUSE_HOME/etc/system.properties`中配置。

###### 使用JMX身份验证 {#Using_JMX_Authentication}
如果要使用jconsole或其他外部工具通过RMI远程连接到JMX，则可能需要JMX身份验证。 否则最好使用hawt.io/jolokia，因为jolokia代理默认安装在hawt.io中。 有关详细信息，请参阅[Hawtio管理控制台](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_hawtio)。

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

要使用Keycloak保护Hawtio管理控制台，请完成以下步骤：

1. 在您的领域的Keycloak管理控制台中创建一个客户端。 例如，在Keycloak`demo`realm中，创建一个客户端`hawtio-client`，指定`public`作为Access Type，并指定一个指向Hawtio的重定向URI：http://localhost:8181/hawtio/*。 配置相应的Web Origin(在本例中为http://localhost:8181)。 设置客户端范围映射，在`hawtio-client`客户端详细信息中的*scope*选项卡中包含*view-profile* client角色*account* client。

2. 使用类似于下面示例中所示的内容在`$FUSE_HOME/etc`目录中创建`keycloak-hawtio-client.json`文件。 根据您的Keycloak环境更改`realm`，`resource`和`auth-server-url`属性。 `resource`属性必须指向上一步中创建的客户端。 该文件由客户端（Hawtio JavaScript应用程序）使用。

   ```json
   {
     "realm" : "demo",
     "clientId" : "hawtio-client",
     "url" : "http://localhost:8080/auth",
     "ssl-required" : "external",
     "public-client" : true
   }
   ```

3. 使用类似于下面示例中所示的内容在`$ FUSE_HOME/etc`目录中创建`keycloak-direct-access.json`文件。 根据您的Keycloak环境更改`realm`和`url`属性。 该文件由JavaScript客户端使用。

   ```json
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

4. 使用类似于下面示例中所示的内容在`$FUSE_HOME/etc` 目录中创建`keycloak-hawtio.json` 文件。 根据您的Keycloak环境更改`realm`和`auth-server-url`属性。 此文件由服务器（JAAS登录模块）端的适配器使用。

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

5. 启动JBoss Fuse 7.2.0，[安装Keycloak功能](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_install_feature)。 然后输入Karaf终端：

   ```properties
   system:property -p hawtio.keycloakEnabled true
   system:property -p hawtio.realm keycloak
   system:property -p hawtio.keycloakClientConfig file://\${karaf.base}/etc/keycloak-hawtio-client.json
   system:property -p hawtio.rolePrincipalClasses org.keycloak.adapters.jaas.RolePrincipal,org.apache.karaf.jaas.boot.principal.RolePrincipal
   restart io.hawt.hawtio-war
   ```

6. 转到<http://localhost:8181/hawtio>并以Keycloak领域的用户身份登录。

   请注意，用户需要具有适当的领域角色才能成功向Hawtio进行身份验证。 可用角色在`hawtio.roles`中的`$FUSE_HOME/etc/system.properties`文件中配置。

#### 2.1.6. Spring Boot 适配器 {#Spring_Boot_Adapter}

为了能够保护Spring Boot应用程序，您必须将Keycloak Spring Boot适配器JAR添加到您的应用程序。 然后，您必须通过正常的Spring Boot配置（`application.properties`）提供一些额外的配置。 我们来看看这些步骤。

##### 适配器安装 {#Adapter_Installation}

Keycloak Spring Boot适配器利用Spring Boot的自动配置，因此您只需将Keycloak Spring Boot启动器添加到您的项目中即可。 他们的Keycloak Spring Boot Starter也可以从[Spring Start Page](https://start.spring.io/)直接获得。 要使用Maven手动添加它，请将以下内容添加到依赖项中：

```xml
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-spring-boot-starter</artifactId>
</dependency>
```

添加适配器BOM依赖项：

```xml
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

目前支持以下嵌入式容器，如果使用Starter，则不需要任何额外的依赖项：

- Tomcat
- Undertow
- Jetty

##### 必需的Spring Boot Adapter配置 {#Required_Spring_Boot_Adapter_Configuration}

本节介绍如何配置Spring Boot应用程序以使用Keycloak。

不需要配置`keycloak.json`文件，您可以通过正常的Spring Boot配置为Spring Boot Keycloak适配器配置域。 例如：

```properties
keycloak.realm = demorealm
keycloak.auth-server-url = http://127.0.0.1:8080/auth
keycloak.ssl-required = external
keycloak.resource = demoapp
keycloak.credentials.secret = 11111111-1111-1111-1111-111111111111
keycloak.use-resource-role-mappings = true
```

您可以通过设置`keycloak.enabled=false`来禁用Keycloak Spring Boot Adapter（例如在测试中）。

要配置策略强制实施器，与keycloak.json不同，必须使用`policy-enforcer-config`而不仅仅是`policy-enforcer`。

您还需要指定通常位于`web.xml`中的Java EE安全配置。 Spring Boot Adapter将`login-method`设置为`KEYCLOAK`并在启动时配置`security-constraints`。 这是一个示例配置：

```properties
keycloak.securityConstraints[0].authRoles[0] = admin
keycloak.securityConstraints[0].authRoles[1] = user
keycloak.securityConstraints[0].securityCollections[0].name = insecure stuff
keycloak.securityConstraints[0].securityCollections[0].patterns[0] = /insecure

keycloak.securityConstraints[1].authRoles[0] = admin
keycloak.securityConstraints[1].securityCollections[0].name = admin stuff
keycloak.securityConstraints[1].securityCollections[0].patterns[0] = /admin
```

> 如果您计划将Spring应用程序部署为WAR，那么不应该使用Spring引导适配器，而应该为您正在使用的应用程序服务器或servlet容器使用专用适配器。您的Spring Boot还应该包含一个 `web.xml`文件。

#### 2.1.7. Tomcat 6, 7 and 8 适配器 {#Tomcat_6_7_and_8_Adapters}

为了能够保护部署在Tomcat 6,7和8上的WAR应用程序，您必须在Tomcat安装中安装Keycloak Tomcat 6,7或8适配器。 然后，您必须在部署到Tomcat的每个WAR中提供一些额外的配置。 我们来看看这些步骤。

##### 适配器安装 {#Adapter_Installation}

适配器不再包含在设备或war分发版中。每个适配器在Keycloak下载站点上都是单独的下载。它们也可以作为maven构件使用。

您必须将适配器发行版解压到Tomcat的`lib/`目录中。在WEB-INF/lib目录中包含适配器的jar将不起作用!Keycloak适配器实现为一个Valve，而Valve代码必须驻留在Tomcat的主lib/目录中。

```bash
$ cd $TOMCAT_HOME/lib
$ unzip keycloak-tomcat6-adapter-dist.zip
    or
$ unzip keycloak-tomcat7-adapter-dist.zip
    or
$ unzip keycloak-tomcat8-adapter-dist.zip
```

##### 每个WAR配置所需 {#Required_Per_WAR_Configuration}

本节描述如何通过在WAR包中添加配置和编辑文件来直接保护WAR。

您必须做的第一件事是在WAR包中创建一个`META-INF/context.xml`文件。 这是一个特定于Tomcat的配置文件，您必须定义一个Keycloak特定的Valve。

```xml
<Context path="/your-context-path">
    <Valve className="org.keycloak.adapters.tomcat.KeycloakAuthenticatorValve"/>
</Context>
```

接下来，您必须在WAR的`WEB-INF`目录中创建一个`keycloak.json`适配器配置文件。

该配置文件的格式在[Java适配器配置](https://www.keycloak.org/docs/latest/securing_apps/index.html#_java_adapter_config)中进行了描述

最后，您必须同时指定`login-config`并使用标准servlet安全性来指定URL上的角色基础约束。 这是一个例子：

```xml
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

#### 2.1.8. Jetty 9.x 适配器 {#Jetty_9_x_Adapters}

Keycloak有一个单独的适配器，适用于Jetty 9.2.x，Jetty 9.3.x和Jetty 9.4.x，您必须安装到Jetty安装中。 然后，您必须在部署到Jetty的每个WAR中提供一些额外的配置。 我们来看看这些步骤。

##### 适配器安装 {#Adapter_Installation}
适配器不再包含在设备或war分发版中。每个适配器在Keycloak下载站点上都是单独的下载。它们也可以作为maven构件使用。

您必须将Jetty 9.x发行版解压缩到Jetty 9.x的[基本目录](https://www.eclipse.org/jetty/documentation/current/startup-base-and-home.html)。 在WEB-INF/lib目录中包含适配器的jar将不起作用！ 在下面的例子中，Jetty基本名为`your-base`：

```bash
$ cd your-base
$ unzip keycloak-jetty93-adapter-dist-2.5.0.Final.zip
```

接下来，您必须为Jetty基础启用`keycloak`模块：

```bash
$ java -jar $JETTY_HOME/start.jar --add-to-startd=keycloak
```

##### 每个WAR配置必需 {#Required_Per_WAR_Configuration}
本节介绍如何通过在WAR包中添加配置和编辑文件来直接保护WAR。

您必须做的第一件事是在WAR包中创建一个`WEB-INF/jetty-web.xml`文件。 这是Jetty特定的配置文件，您必须在其中定义Keycloak特定的身份验证器。

```xml
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

接下来，您必须在WAR的`WEB-INF`目录中创建一个`keycloak.json`适配器配置文件。

该配置文件的格式在[Java适配器配置](https://www.keycloak.org/docs/latest/securing_apps/index.html#_java_adapter_config) 部分中进行了描述。

> Jetty 9.x适配器将无法找到`keycloak.json`文件。 您必须在`jetty-web.xml`文件中定义所有适配器设置，如下所述。

您可以在`jetty-web.xml`中定义所有内容，而不是使用keycloak.json。 您只需要弄清楚json设置如何与`org.keycloak.representations.adapters.config.AdapterConfig`类匹配。

```xml
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

您不必破解打开WAR以使用keycloak保护它。 而是使用yourwar.xml的名称在webapps目录中创建jetty-web.xml文件。 Jetty应该捡起来。 在此模式下，您必须直接在xml文件中声明keycloak.json配置。

最后，您必须同时指定`login-config`并使用标准servlet安全性来指定URL上的角色基础约束。 这是一个例子：

```xml
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

#### 2.1.9. Spring Security 适配器 {#Spring_Security_Adapter}

要使用Spring Security和Keycloak保护应用程序，请将此适配器添加为项目的依赖项。 然后，您必须在Spring Security配置文件中提供一些额外的bean，并将Keycloak安全过滤器添加到管道中。

与其他Keycloak Adapters不同，您不应在web.xml中配置安全性。 但是，仍然需要keycloak.json。

##### 适配器安装 {#Adapter_Installation}
添加Keycloak Spring Security适配器作为Maven POM或Gradle构建的依赖项。

```xml
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-spring-security-adapter</artifactId>
    <version>6.0.1</version>
</dependency>
```

##### Spring安全配置 {#Spring_Security_Configuration}
Keycloak Spring Security适配器利用Spring Security灵活的安全配置语法。

###### Java配置 {#Java_Configuration}
Keycloak提供了一个KeycloakWebSecurityConfigurerAdapter作为创建[WebSecurityConfigurer](https://docs.spring.io/spring-security/site/docs/4.0.x/apidocs/org/springframework/security/config/annotation/web/WebSecurityConfigurer.html)实例。 该实现允许通过重写方法进行自定义。 虽然不需要使用它，但它极大地简化了安全上下文配置。

```java
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

您必须为公共或机密应用程序提供一个会话身份验证策略bean，其类型应为`RegisterSessionAuthenticationStrategy`，对于仅承载应用程序，您必须提供`NullAuthenticatedSessionStrategy`。

目前不支持Spring Security的`SessionFixationProtectionStrategy`，因为它在通过Keycloak登录后更改会话标识符。 如果会话标识符更改，则通用注销将不起作用，因为Keycloak不知道新的会话标识符。

> `@KeycloakConfiguration`注释是一个元数据注释，它定义了在Spring Security中集成Keycloak所需的所有注释。 如果你有一个复杂的Spring Security设置，你可以简单地看一下`@KeycloakConfiguration`注释的注释，并创建你自己的自定义元注释，或者只是为Keycloak适配器使用特定的Spring注释。

###### XML配置 {#XML_Configuration}
虽然Spring Security的XML命名空间简化了配置，但自定义配置可能有点冗长。

```xml
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

##### 多租户 {#Multi_Tenancy}
Keycloak Spring Security适配器还支持多租户。 而不是使用`keycloak.json`的路径注入`AdapterDeploymentContextFactoryBean`，您可以注入`KeycloakConfigResolver`接口的实现。 有关如何实现`KeycloakConfigResolver`的更多详细信息，请参见[多租户](https://www.keycloak.org/docs/latest/securing_apps/index.html#_multi_tenancy)。

##### 命名安全角色 {#Naming_Security_Roles}
使用基于角色的身份验证时，Spring Security要求角色名称以`ROLE_`开头。 例如，必须在Keycloak中将管理员角色声明为`ROLE_ADMIN`或类似名称，而不仅仅是`ADMIN`。

类`org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider`支持可选的`org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper`，它可用于将来自Keycloak的角色映射到Spring Security认可的角色。 例如，使用`org.springframework.security.core.authority.mapping.SimpleAuthorityMapper`插入`ROLE_`前缀并将角色名称转换为大写。 该类是Spring Security Core模块的一部分。

##### 客户端到客户端的支持 {#Client_to_Client_Support}
为了简化客户端之间的通信，Keycloak提供了Spring的`RestTemplate`的扩展，为您处理承载令牌认证。 要启用此功能，您的安全配置必须添加`KeycloakRestTemplate`bean。 请注意，它必须作为原型确定范围才能正常运行。

对于Java配置：

```java
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

对于XML配置：

```xml
<bean id="keycloakRestTemplate" class="org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate" scope="prototype">
    <constructor-arg name="factory" ref="keycloakClientRequestFactory" />
</bean>
```

然后，只要需要调用另一个客户端，您的应用程序代码就可以使用`KeycloakRestTemplate`。 例如：

```java
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

##### Spring Boot集成 {#Spring_Boot_Integration}
可以组合Spring Boot和Spring Security适配器。

如果您使用Keycloak Spring Boot Starter来使用Spring Security适配器，您只需添加Spring Security启动器：

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

###### 使用Spring Boot配置 {#Using_Spring_Boot_Configuration}
默认情况下，Spring Security Adapter会查找`keycloak.json`配置文件。 您可以通过添加此bean来确保它查看Spring Boot Adapter提供的配置：

```java
@Bean
public KeycloakConfigResolver KeycloakConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
}
```

###### 避免Bean注册两次 {#Avoid_double_bean_registration}
Spring Boot尝试使用Web应用程序上下文急切地注册过滤器bean。 因此，在Spring Boot环境中运行Keycloak Spring Security适配器时，可能需要将`FilterRegistrationBean`添加到安全配置中，以防止Keycloak过滤器被注册两次。

Spring Boot 2.1默认情况下也会禁用`spring.main.allow-bean-definition-overriding`。 这可能意味着如果扩展`KeycloakWebSecurityConfigurerAdapter`的`Configuration`类注册了一个已被`@ComponentScan`检测到的bean，则会遇到`BeanDefinitionOverrideException`。 通过覆盖注册以使用特定于Boot的`@ConditionalOnMissingBean`注释可以避免这种情况，如下面的`HttpSessionManager`。

```java
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

#### 2.1.10. Java Servlet Filter 适配器 {#Java_Servlet_Filter_Adapter}

如果要在没有Keycloak适配器的平台上部署Java Servlet应用程序，则选择使用servlet过滤器适配器。 此适配器的工作方式与其他适配器略有不同。 您没有在web.xml中定义安全性约束。 而是使用Keycloak servlet过滤器适配器定义过滤器映射，以保护要保护的URL模式。

> Backchannel注销与标准适配器的工作方式略有不同。 它不会使HTTP会话无效，而是将会话ID标记为已注销。 没有标准方法可以根据会话ID使HTTP会话无效。

```xml
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

在上面的代码片段中有两个url模式。 `/protected/*` 是我们想要保护的文件，而`/keycloak/*` url-pattern处理来自Keycloak服务器的回调。

如果需要在配置的`url-patterns`下面排除一些路径，可以使用Filter init-param`keycloak.config.skipPattern`来配置一个正则表达式，该表达式描述了keycloak过滤器应立即委托给的路径模式。 过滤链。 默认情况下，不配置skipPattern。

模式与`requestURI`匹配，没有`context-path`。 给定context-path `/myapp`，对`/myapp/index.html`的请求将与`/index.html`匹配跳过模式。

```xml
<init-param>
    <param-name>keycloak.config.skipPattern</param-name>
    <param-value>^/(path1|path2|path3).*</param-value>
</init-param>
```

请注意，您应该在Keycloak管理控制台中使用管理URL配置客户端，该URL指向过滤器的url-pattern所涵盖的安全部分。

管理员URL将对管理员URL进行回调，以执行backchannel注销等操作。 因此，此示例中的Admin URL应为`http[s]://hostname/{context-root}/keycloak`。

Keycloak过滤器具有与其他适配器相同的配置参数，除非您必须将它们定义为过滤器init参数而不是上下文参数。

要使用此过滤器，请在WAR poms中包含此maven工件：

```xml
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-servlet-filter-adapter</artifactId>
    <version>6.0.1</version>
</dependency>
```

##### 在OSGi上使用 {#Using_on_OSGi}
servlet过滤器适配器打包为OSGi包，因此可以在具有HTTP服务和HTTP白板的通用OSGi环境（R6及更高版本）中使用。

###### 安装 {#Installation}
适配器及其依赖项作为Maven工件分发，因此您需要使用Internet连接来访问Maven Central，或者将工件缓存在本地Maven存储库中。

如果您使用的是Apache Karaf，则可以直接从Keycloak功能仓库安装一个功能：

```
karaf@root()> feature:repo-add mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
karaf@root()> feature:install keycloak-servlet-filter-adapter
```

对于其他OSGi运行时，请参阅运行时文档，了解如何安装适配器包及其依赖项。

> 如果你的OSGi平台是带有Pax Web的Apache Karaf，你应该考虑使用[JBoss Fuse 6](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse_adapter)或[JBoss Fuse 7](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_adapter)适配器。

###### 配置 {#Configuration}
首先，需要使用OSGi HTTP服务将适配器注册为servlet过滤器。 最常见的方法是编程（例如通过bundle激活器）和声明（使用OSGi注释）。 我们建议使用后者，因为它简化了动态注册和取消注册过滤器的过程：

```java
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

上面的代码片段使用OSGi声明性服务规范将过滤器公开为`javax.servlet.Filter`类下的OSGI服务。 一旦在OSGi服务注册表中发布了类，它将由OSGi HTTP服务实现获取并用于过滤对指定servlet上下文的请求。 这将为每个匹配servlet上下文路径+过滤器路径的请求触发Keycloak适配器。

由于组件处于OSGi Configuration Admin Service的控制之下，因此可以动态配置其属性。 为此，要在OSGi运行时的标准配置位置下创建一个`mypackage.KeycloakFilter.cfg`文件：

```properties
keycloak.config.file = /path/to/keycloak.json
osgi.http.whiteboard.filter.pattern = /secure/*
```

或者使用交互式控制台，如果您的运行时允许：

```
karaf@root()> config:edit mypackage.KeycloakFilter
karaf@root()> config:property-set keycloak.config.file '${karaf.etc}/keycloak.json'
karaf@root()> config:update
```

如果您需要更多控制权，例如 提供自定义`KeycloakConfigResolver`来实现[多租户](https://www.keycloak.org/docs/latest/securing_apps/index.html#_multi_tenancy)，您可以通过编程方式注册过滤器：

```java
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

有关程序化注册的更多信息，请参阅[Apache Felix HTTP服务](http://felix.apache.org/documentation/subprojects/apache-felix-http-service.html#using-the-osgi-http-whiteboard)。

#### 2.1.11. JAAS 插件 {#JAAS_plugin}

通常不需要在大多数应用程序中使用JAAS，特别是如果它们是基于HTTP的，并且您最有可能选择我们的其他适配器之一。 但是，某些应用程序和系统仍可能依赖纯粹的传统JAAS解决方案。 Keycloak提供了两个登录模块来帮助解决这些问题。

提供的登录模块是：

- org.keycloak.adapters.jaas.DirectAccessGrantsLoginModule

  此登录模块允许使用Keycloak中的用户名/密码进行身份验证。 它使用[资源所有者密码凭证](https://www.keycloak.org/docs/latest/securing_apps/index.html#_resource_owner_password_credentials_flow)流来验证提供的用户名/密码是否有效。 它对非基于Web的系统非常有用，它需要依赖JAAS并希望使用Keycloak，但由于其非Web性质而无法使用基于标准浏览器的流程。 此类应用程序的示例可以是消息传递或SSH。

- org.keycloak.adapters.jaas.BearerTokenLoginModule

  此登录模块允许通过CallbackHandler作为密码传递给它的Keycloak访问令牌进行身份验证。 例如，当您从基于标准的身份验证流程获得Keycloak访问令牌并且您的Web应用程序需要与依赖于JAAS的外部非基于Web的系统进行通信时，它可能很有用。 例如，消息传递系统。

两个模块都使用以下配置属性：

- keycloak-config-file

  `keycloak.json`配置文件的位置。 配置文件可以位于文件系统上，也可以位于类路径上。 如果它位于类路径上，则需要在该位置前加上`classpath:`（例如`classpath:/path/keycloak.json`）。 这是*REQUIRED*

- `role-principal-class`

  为附加到JAAS Subject的Role主体配置备用类。 默认值为`org.keycloak.adapters.jaas.RolePrincipal`。 注意：该类需要具有带有单个`String`参数的构造函数。

- `scope`

  此选项仅适用于`DirectAccessGrantsLoginModule`。 指定的值将用作资源所有者密码凭据授予请求中的OAuth2 `scope` 参数。

#### 2.1.12. CLI / Desktop 应用 {#CLI___Desktop_Applications}

Keycloak支持通过系统浏览器执行身份验证步骤，通过`KeycloakInstalled`adapter保护桌面（例如Swing，JavaFX）或CLI应用程序。

`KeycloakInstalled`适配器支持`desktop`和`manual`变体。 桌面版本使用系统浏览器来收集用户凭据。 手动变体从`STDIN`读取用户凭据。

提示：Google在[OAuth2InstalledApp](https://developers.google.com/identity/protocols/OAuth2InstalledApp)上提供了有关此方法的更多信息。

##### 它是如何工作的 {#How_it_works}
为了使用`desktop`变体对用户进行身份验证，`KeycloakInstalled`适配器打开一个桌面浏览器窗口，当在`KeycloakInstalled`对象上调用`loginDesktop()`方法时，用户使用常规的Keycloak登录页面进行登录。

使用redirect参数打开登录页面URL，该参数指向本地`ServerSocket`，该地址`ServerSocket`在由适配器启动的`localhost`上的空闲临时端口上进行侦听。

成功登录后，`KeycloakInstalled`从传入的HTTP请求接收授权代码并执行授权代码流。 一旦令牌交换的代码完成，`ServerSocket`就会关闭。

> 如果用户已经有一个活动的Keycloak会话，则不会显示登录表单，但会继续进行令牌交换的代码，这样可以实现基于Web的SSO平滑体验。

客户端最终会收到令牌（access_token，refresh_token，id_token），然后可以使用这些令牌来调用后端服务。

`KeycloakInstalled`适配器支持更新陈旧令牌。

##### 适配器安装 {#Adapter_Installation}
```xml
<dependency>
        <groupId>org.keycloak</groupId>
        <artifactId>keycloak-installed-adapter</artifactId>
        <version>6.0.1</version>
</dependency>
```

##### 客户端配置 {#Client_Configuration}
应用程序需要配置为`public` OpenID Connect客户端，其中`Standard Flow Enabled`和 http://localhost:*作为允许的`Valid Redirect URI`。

##### 用法 {#Usage}
`KeycloakInstalled`适配器从类路径上的`META-INF/keycloak.json`读取它的配置。 可以通过`KeycloakInstalled`constructor为`InputStream`或`KeycloakDeployment`提供自定义配置。

在下面的示例中，`desktop-app`的客户端配置使用以下`keycloak.json`：

```json
{
  "realm": "desktop-app-auth",
  "auth-server-url": "http://localhost:8081/auth",
  "ssl-required": "external",
  "resource": "desktop-app",
  "public-client": true,
  "use-resource-role-mappings": true
}
```

下面的草图演示了如何使用`KeycloakInstalled`适配器：

```java
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

> `KeycloakInstalled`类支持通过`loginResponseWriter`和`logoutResponseWriter`属性自定义登录/注销请求返回的http响应。

##### 例子 {#Example}
以下提供了上述配置的示例。

```java
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

#### 2.1.13. 安全上下文 {#Security_Context}

如果您需要直接访问令牌，则可以使用`KeycloakSecurityContext`接口。 如果要从令牌中检索其他详细信息（例如用户配置文件信息），或者要调用受Keycloak保护的RESTful服务，这可能很有用。

在servlet环境中，它在安全调用中可用作HttpServletRequest中的属性：

```java
httpServletRequest
    .getAttribute(KeycloakSecurityContext.class.getName());
```

或者，它在HttpSession中的不安全请求中可用：

```
httpServletRequest.getSession()
    .getAttribute(KeycloakSecurityContext.class.getName());
```

#### 2.1.14. 错误处理 {#Error_Handling}

Keycloak为基于servlet的客户端适配器提供了一些错误处理功能。 在身份验证中遇到错误时，Keycloak将调用`HttpServletResponse.sendError()`。 您可以在`web.xml`文件中设置一个错误页面来处理您想要的错误。 Keycloak可能会丢失400,401,403和500错误。

```xml
<error-page>
    <error-code>403</error-code>
    <location>/ErrorHandler</location>
</error-page>
```

Keycloak还设置了一个可以检索的`HttpServletRequest`属性。 属性名称是`org.keycloak.adapters.spi.AuthenticationError`，应该将其转换为`org.keycloak.adapters.OIDCAuthenticationError`。

例如：

```java
import org.keycloak.adapters.OIDCAuthenticationError;
import org.keycloak.adapters.OIDCAuthenticationError.Reason;
...

OIDCAuthenticationError error = (OIDCAuthenticationError) httpServletRequest
    .getAttribute('org.keycloak.adapters.spi.AuthenticationError');

Reason reason = error.getReason();
System.out.println(reason.name());
```

#### 2.1.15. 注销 {#Logout}

您可以通过多种方式注销Web应用程序。 对于Java EE servlet容器，可以调用`HttpServletRequest.logout()`。 对于其他浏览器应用程序，您可以将浏览器重定向到`http://auth-server/auth/realms/{realm-name}/protocol/openid-connect/logout?redirect_uri=encodedRedirectUri`，如果您有与您的浏览器进行SSO会话。

当使用`HttpServletRequest.logout()`选项时，适配器对传递刷新令牌的Keycloak服务器执行反向通道POST调用。 如果该方法是从未受保护的页面（不检查有效令牌的页面）执行的，则刷新令牌可能不可用，在这种情况下，适配器会跳过该调用。 因此，建议使用受保护的页面来执行`HttpServletRequest.logout()`，以便始终考虑当前令牌，并在需要时执行与Keycloak服务器的交互。

如果您希望在注销过程中避免注销外部身份提供程序，则可以提供参数`initiating_idp`，其值为相关身份提供程序的标识（别名）。 当注销端点作为外部身份提供程序启动的单一注销的一部分进行调用时，这非常有用。

#### 2.1.16. 参数转发 {#Parameters_Forwarding}

Keycloak初始授权端点请求支持各种参数。 大多数参数在[OIDC规范](https://openid.net/specs/openid-connect-core-1_0.html#AuthorizationEndpoint)中描述。 某些参数由适配器根据适配器配置自动添加。 但是，还可以在每次调用的基础上添加一些参数。 打开受保护的应用程序URI时，特定参数将转发到Keycloak授权端点。

例如，如果您请求脱机令牌，则可以使用`scope`参数打开受保护的应用程序URI，如：

```
http://myappserver/mysecuredapp?scope=offline_access
```

并且参数`scope = offline_access`将自动转发到Keycloak授权端点。

支持的参数是：

- scope - 使用以空格分隔的范围列表。 以空格分隔的列表通常引用在特定客户端上定义的[客户端范围](https://www.keycloak.org/docs/6.0/server_admin/#_client_scopes)。 请注意，范围`openid`将始终由适配器添加到作用域列表中。 例如，如果输入范围选项`address phone`，则对Keycloak的请求将包含范围参数`scope=openid address phone`。
- prompt - Keycloak支持以下设置：
  - `login` - 即使用户已经过身份验证，也会忽略SSO并始终显示Keycloak登录页面
  - `consent` - 仅适用于`Consent Required(需要同意)`的客户。 如果使用它，即使用户先前已同意此客户端，也将始终显示“同意”页面。
  - `none` - 登录页面永远不会显示; 相反，如果用户尚未通过身份验证，则会将用户重定向到应用程序，并显示错误。 此设置允许您在应用程序端创建过滤器/拦截器，并向用户显示自定义错误页面。 请参阅规范中的更多详细信息。
- max_age - 仅在用户已经过身份验证时使用。 指定身份验证持续的最长允许时间（从用户进行身份验证时开始计算）。 如果用户的身份验证时间超过`maxAge`，则会忽略SSO并且必须重新进行身份验证。
- login_hint - 用于预填充登录表单上的用户名/电子邮件字段。
- kc_idp_hint - 用于告诉Keycloak跳过显示登录页面并自动重定向到指定的身份提供者。 [身份提供商文档](https://www.keycloak.org/docs/6.0/server_admin/#_client_suggested_idp)中的更多信息。

大多数参数在[OIDC规范](https://openid.net/specs/openid-connect-core-1_0.html#AuthorizationEndpoint)中描述。 唯一的例外是参数`kc_idp_hint`，它特定于Keycloak并包含要自动使用的身份提供者的名称。 有关更多信息，请参阅[服务器管理指南](https://www.keycloak.org/docs/6.0/server_admin/)中的`Identity Brokering`部分。

> 如果使用附加参数打开URL，则如果已在应用程序中进行了身份验证，则适配器不会将您重定向到Keycloak。 例如，如果您已经对应用程序`mysecredapp`进行了身份验证，则打开`http://myappserver/mysecuredapp?prompt=login`将不会自动将您重定向到Keycloak登录页面。 将来可能会更改此行为。

#### 2.1.17. 客户端身份验证 {#Client_Authentication}

当机密OIDC客户端需要发送反向信道请求（例如，交换令牌代码或刷新令牌）时，它需要针对Keycloak服务器进行身份验证。 默认情况下，有三种方法可以对客户端进行身份验证：客户端ID和客户端密钥，使用签名JWT的客户端身份验证，或使用客户端密钥的签名JWT进行客户端身份验证。

##### 客户端ID和客户端Secret {#Client_ID_and_Client_Secret}
这是OAuth2规范中描述的传统方法。 客户端有一个secret，需要知道适配器（应用程序）和Keycloak服务器。 您可以在Keycloak管理控制台中为特定客户端生成密码，然后将此密钥粘贴到应用程序端的`keycloak.json`文件中：

```java
"credentials": {
    "secret": "19666a4f-32dd-4049-b082-684c74115f28"
}
```

##### 使用签名JWT的客户端身份验证 {#Client_Authentication_with_Signed_JWT}
这基于[RFC7523](https://tools.ietf.org/html/rfc7523)规范。 它以这种方式工作：

- 客户端必须具有私钥和证书。 对于Keycloak，可以通过传统的`keystore`文件获得，该文件可以在客户端应用程序的类路径上或文件系统上的某个位置获得。
- 启动客户端应用程序后，它允许使用URL等[JWKS](https://self-issued.info/docs/draft-ietf-jose-json-web-key.html)格式下载其公钥。 作为http://myhost.com/myapp/k_jwks，假设http://myhost.com/myapp是客户端应用程序的基本URL。 Keycloak可以使用此URL（见下文）。
- 在身份验证期间，客户端生成JWT令牌并使用其私钥对其进行签名，并在`client_assertion`参数中的特定反向通道请求（例如，代码到令牌请求）中将其发送到Keycloak。
- Keycloak必须具有客户端的公钥或证书，以便它可以验证JWT上的签名。 在Keycloak中，您需要为客户端配置客户端凭据。 首先，您需要选择`Signed JWT`作为在管理控制台的“凭据”选项卡中验证客户端的方法。 然后你可以选择：
  - 配置JWKS URL，Keycloak可以在其中下载客户端的公钥。 这可以是诸如http://myhost.com/myapp/k_jwks之类的URL（参见上面的详细信息）。 此选项是最灵活的，因为客户端可以随时旋转其键，然后Keycloak总是在需要时下载新密钥而无需更改配置。 更准确地说，Keycloak在看到由未知的`kid`（密钥ID）签名的令牌时下载新密钥。
  - 以PEM格式，JWK格式或从密钥库上载客户端的公钥或证书。 使用此选项，公钥是硬编码的，必须在客户端生成新密钥对时进行更改。 如果您没有自己的密钥库，您甚至可以从Keycloak管理控制台生成自己的密钥库。 有关如何设置Keycloak管理控制台的更多详细信息，请参阅[服务器管理指南](https://www.keycloak.org/docs/6.0/server_admin/)。

要在适配器端进行设置，您需要在`keycloak.json`文件中使用这样的内容：

```json
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

使用此配置，密钥库文件`keystore-client.jks`必须在WAR中的类路径上可用。 如果不使用前缀`classpath:`，则可以指向运行客户端应用程序的文件系统上的任何文件。

为了获得灵感，您可以查看示例分发到主要演示示例中的`product-portal`应用程序。

##### 使用客户端密钥使用签名JWT进行客户端身份验证 {#Client_Authentication_with_Signed_JWT_using_Client_Secret}
这与使用签名JWT的客户端身份验证相同，除了使用客户端密钥而不是私钥和证书。

客户端有一个`secret(秘密)`，需要知道适配器（应用程序）和Keycloak服务器。 您需要选择`Signed JWT with Client Secret`作为在管理控制台的`Credentials凭据`选项卡中验证客户端的方法，然后将此`secret(秘密)`粘贴到应用程序端的`keycloak.json`文件中：

```json
"credentials": {
  "secret-jwt": {
    "secret": "19666a4f-32dd-4049-b082-684c74115f28"
  }
}
```

##### 添加自己的客户端身份验证方法 {#Add_Your_Own_Client_Authentication_Method}
您也可以添加自己的客户端身份验证方法。 您将需要实现客户端和服务器端提供程序。 有关更多详细信息，请参阅[服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/)中的`Authentication SPI`部分。

#### 2.1.18. 多租户 {#Multi_Tenancy}

在我们的上下文中，多租户意味着可以使用多个Keycloak领域来保护单个目标应用程序（WAR）。 领域可以位于同一个Keycloak实例中，也可以位于不同的实例上。

实际上，这意味着应用程序需要有多个`keycloak.json`适配器配置文件。

您可以拥有多个WAR实例，并将不同的适配器配置文件部署到不同的上下文路径。 但是，这可能不方便，您可能还希望基于上下文之外的其他内容选择领域。

Keycloak可以使用自定义配置解析器，因此您可以选择每个请求使用的适配器配置。

要实现此目的，首先需要创建`org.keycloak.adapters.KeycloakConfigResolver`的实现。 例如：

```java
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

您还需要配置与`web.xml`中的`keycloak.config.resolver`context-param一起使用的`KeycloakConfigResolver`实现：

```xml
<web-app>
    ...
    <context-param>
        <param-name>keycloak.config.resolver</param-name>
        <param-value>example.PathBasedKeycloakConfigResolver</param-value>
    </context-param>
</web-app>
```

#### 2.1.19. 应用程序集群 {#Application_Clustering}

本章涉及支持部署到JBoss EAP，WildFly和JBoss AS的集群应用程序。

根据您的应用程序是否有以下几种选项：

- 无状态或有状态
- 可分发的(复制的http会话)或不可分发的
- 依赖负载均衡器提供的粘性会话
- 与Keycloak在同一个域名中托管

处理群集并不像普通应用程序那么简单。 主要是因为浏览器和服务器端应用程序都向Keycloak发送请求，因此它不像在负载均衡器上启用粘性会话那么简单。

##### 无状态令牌存储 {#Stateless_token_store}
默认情况下，Keycloak保护的Web应用程序使用HTTP会话来存储安全上下文。 这意味着您必须启用粘性会话或复制HTTP会话。

作为在HTTP会话中存储安全上下文的替代方法，可以将适配器配置为将其存储在cookie中。 如果要使应用程序无状态或者您不希望在HTTP会话中存储安全上下文，这将非常有用。

要使用cookie存储来保存安全上下文，请编辑应用程序`WEB-INF/keycloak.json`并添加：

```
"token-store": "cookie"
```

> `token-store`的默认值是`session`，它将安全上下文存储在HTTP会话中。

使用cookie存储的一个限制是整个安全上下文在cookie中传递给每个HTTP请求。 这可能会影响性能。

另一个小限制是对单点登出的有限支持。 如果您从应用程序本身初始化servlet注销（HttpServletRequest.logout），它将正常工作，因为适配器将删除KEYCLOAK_ADAPTER_STATE cookie。 但是，Keycloak不会将从其他应用程序初始化的反向通道注销传播到使用cookie存储的应用程序。 因此，建议对访问令牌超时使用短值（例如1分钟）。

> 某些负载均衡器不允许任何配置粘性会话cookie名称或内容，例如Amazon ALB。 对于这些，建议将`shouldAttachRoute`选项设置为`false`。

##### 相对URI优化 {#Relative_URI_optimization}
在Keycloak和应用程序托管在同一域（通过反向代理或负载平衡器）的部署方案中，在客户端配置中使用相对URI选项会很方便。

使用相对URI，URI将相对于用于访问Keycloak的URL进行解析。

例如，如果您的应用程序的URL是`https://acme.org/myapp`，而Keycloak的URL是`https://acme.org/auth`，那么您可以使用redirect-uri `/myapp` 而不是 `https://acme.org/myapp`。

##### 管理员URL配置 {#Admin_URL_configuration}
可以在Keycloak管理控制台中配置特定客户端的管理URL。 它由Keycloak服务器用于向应用程序发送后端请求以执行各种任务，例如注销用户或推送撤销策略。

例如，反向通道注销的工作方式是：

1. 用户从一个应用程序发送注销请求
2. 应用程序向Keycloak发送注销请求
3. Keycloak服务器使用户会话无效
4. 然后，Keycloak服务器向应用程序发送反向通道请求，其中包含与会话关联的管理URL
5. 当应用程序收到注销请求时，它会使相应的HTTP会话无效

如果admin URL包含 `${application.session.host}` ，它将被替换为与HTTP会话关联的节点的URL。

##### 注册应用程序节点 {#Registration_of_application_nodes}
上一节描述了Keycloak如何向与特定HTTP会话关联的节点发送注销请求。 但是，在某些情况下，管理员可能希望将管理任务传播到所有已注册的群集节点，而不仅仅是其中一个。 例如，将新的not before策略推送到应用程序或从应用程序注销所有用户。

在这种情况下，Keycloak需要知道所有应用程序集群节点，因此它可以将事件发送给所有这些节点。 为此，我们支持自动发现机制：

1. 当新的应用程序节点加入群集时，它会向Keycloak服务器发送注册请求
2. 该请求可以以配置的周期性间隔重新发送到Keycloak
3. 如果Keycloak服务器在指定的超时内未收到重新注册请求，则它会自动取消注册特定节点
4. 当Keycloak发送取消注册请求时，该节点也会在Keycloak中取消注册，这通常是在节点关闭或应用程序取消部署期间。 当未调用未部署侦听器时，这可能无法正常执行强制关闭，这导致需要自动取消注册

默认情况下禁用发送启动注册和定期重新注册，因为只有某些群集应用程序才需要它。

要启用该功能，请编辑应用程序的`WEB-INF/keycloak.json`文件并添加：

```
"register-node-at-startup": true,
"register-node-period": 600,
```

这意味着适配器将在启动时发送注册请求，并每10分钟重新注册一次。

在Keycloak管理控制台中，您可以指定最大节点重新注册超时（应该大于适配器配置中的*register-node-period*）。 您还可以通过管理控制台手动添加和删除群集节点，如果您不想依赖自动注册功能，或者如果您想要在不使用自动注销功能的情况下删除陈旧的应用程序节点，这将非常有用。

##### 刷新每个请求中的令牌 {#Refresh_token_in_each_request}
默认情况下，应用程序适配器仅在访问令牌过期时刷新。 但是，您还可以配置适配器以在每个请求上刷新令牌。 这可能会对性能产生影响，因为您的应用程序将向Keycloak服务器发送更多请求。

要启用该功能，请编辑应用程序的`WEB-INF/keycloak.json`文件并添加：

```
"always-refresh-token": true
```

> 这可能会对性能产生重大影响。 如果您不能依赖反向通道消息传播注销而不是策略之前，则仅启用此功能。 另一件需要考虑的事情是，默认情况下，访问令牌的到期时间很短，因此即使未传播注销，令牌也会在注销后的几分钟内到期。

### 2.2. JavaScript 适配器 {#JavaScript_Adapter}

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

#### 2.2.1. Session Status iframe {#Session_Status_iframe}
By default, the JavaScript adapter creates a hidden iframe that is used to detect if a Single-Sign Out has occurred. This does not require any network traffic, instead the status is retrieved by looking at a special status cookie. This feature can be disabled by setting `checkLoginIframe: false` in the options passed to the `init` method.

You should not rely on looking at this cookie directly. Its format can change and it’s also associated with the URL of the Keycloak server, not your application.

#### 2.2.2. Implicit and Hybrid Flow {#Implicit_and_Hybrid_Flow}
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

#### 2.2.3. Hybrid Apps with Cordova   {#Hybrid_Apps_with_Cordova}
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

#### 2.2.4. Earlier Browsers {#Earlier_Browsers}
The JavaScript adapter depends on Base64 (window.btoa and window.atob), HTML5 History API and optionally the Promise API. If you need to support browsers that do not have these available (for example, IE9) you need to add polyfillers.

Example polyfill libraries:

- Base64 - <https://github.com/davidchambers/Base64.js>
- HTML5 History - <https://github.com/devote/HTML5-History-API>
- Promise - <https://github.com/stefanpenner/es6-promise>

#### 2.2.5. JavaScript Adapter Reference {#JavaScript_Adapter_Reference}
##### Constructor {#Constructor}
```
new Keycloak();
new Keycloak('http://localhost/keycloak.json');
new Keycloak({ url: 'http://localhost/auth', realm: 'myrealm', clientId: 'myApp' });
```

##### Properties {#Properties}
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

##### Methods {#Methods}
###### init(options) {#init_options_}
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

###### login(options) {#login_options_}
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

###### createLoginUrl(options) {#createLoginUrl_options_}
Returns the URL to login form on (options is an optional object with redirectUri and/or prompt fields).

Options is an Object, which supports same options like the function `login` .

###### logout(options) {#logout_options_}
Redirects to logout.

Options is an Object, where:

- redirectUri - Specifies the uri to redirect to after logout.

###### createLogoutUrl(options) {#createLogoutUrl_options_}
Returns the URL to logout the user.

Options is an Object, where:

- redirectUri - Specifies the uri to redirect to after logout.

###### register(options) {#register_options_}
Redirects to registration form. Shortcut for login with option action = 'register'

Options are same as for the login method but 'action' is set to 'register'

###### createRegisterUrl(options) {#createRegisterUrl_options_}
Returns the url to registration page. Shortcut for createLoginUrl with option action = 'register'

Options are same as for the createLoginUrl method but 'action' is set to 'register'

###### accountManagement() {#accountManagement__}
Redirects to the Account Management Console.

###### createAccountUrl() {#createAccountUrl__}
Returns the URL to the Account Management Console.

###### hasRealmRole(role) {#hasRealmRole_role_}
Returns true if the token has the given realm role.

###### hasResourceRole(role, resource) {#hasResourceRole_role,_resource_}
Returns true if the token has the given role for the resource (resource is optional, if not specified clientId is used).

###### loadUserProfile() {#loadUserProfile__}
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

###### isTokenExpired(minValidity) {#isTokenExpired_minValidity_}
Returns true if the token has less than minValidity seconds left before it expires (minValidity is optional, if not specified 0 is used).

###### updateToken(minValidity) {#updateToken_minValidity_}
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

###### clearToken() {#clearToken__}
Clear authentication state, including tokens. This can be useful if application has detected the session was expired, for example if updating token fails.

Invoking this results in onAuthLogout callback listener being invoked.

##### Callback Events {#Callback_Events}
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

### 2.3. Node.js 适配器 {#Node_js_Adapter}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/nodejs-adapter.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/nodejs-adapter.adoc)

Keycloak provides a Node.js adapter built on top of [Connect](https://github.com/senchalabs/connect) to protect server-side JavaScript apps - the goal was to be flexible enough to integrate with frameworks like [Express.js](https://expressjs.com/).

The library can be downloaded directly from [Keycloak organization](https://www.npmjs.com/package/keycloak-connect) and the source is available at [GitHub](https://github.com/keycloak/keycloak-nodejs-connect).

To use the Node.js adapter, first you must create a client for your application in the Keycloak Administration Console. The adapter supports public, confidential, and bearer-only access type. Which one to choose depends on the use-case scenario.

Once the client is created click the `Installation` tab, select `Keycloak OIDC JSON` for `Format Option`, and then click `Download`. The downloaded `keycloak.json` file should be at the root folder of your project.

#### 2.3.1. Installation {#Installation}
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

#### 2.3.2. Usage {#Usage}
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

#### 2.3.3. Installing Middleware {#Installing_Middleware}
Once instantiated, install the middleware into your connect-capable app:

```
    var app = express();

    app.use( keycloak.middleware() );
```

#### 2.3.4. Checking Authentication {#Checking_Authentication}
To check that a user is authenticated before accessing a resource, simply use `keycloak.checkSso()`. It will only authenticate if the user is already logged-in. If the user is not logged-in, the browser will be redirected back to the originally-requested URL and remain unauthenticated:

```
    app.get( '/check-sso', keycloak.checkSso(), checkSsoHandler );
```

#### 2.3.5. Protecting Resources {#Protecting_Resources}
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

#### 2.3.6. Additional URLs {#Additional_URLs}
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

### 2.4. Keycloak 看门人 {#Keycloak_Gatekeeper}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/keycloak-gatekeeper.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/keycloak-gatekeeper.adoc)

Keycloak provides a Go programming language adapter for use with OpenID Connect (OIDC) that supports both access tokens in a browser cookie or bearer tokens.

This documentation details how to build and configure keycloak-gatekeeper followed by details of how to use each of its features.

For further information, see the included help file which includes a full list of commands and switches. View the file by entering the following at the command line (modify the location to match where you install keycloak-gatekeeper):

```
    $ bin/keycloak-gatekeeper help
```

#### 2.4.1. Building {#Building}
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

#### 2.4.2. Configuration options {#Configuration_options}
Configuration can come from a yaml/json file or by using command line options. Here is a list of options.

```
# is the url for retrieve the OpenID configuration - normally the <server>/auth/realm/<realm_name>
discovery-url: https://keycloak.example.com/auth/realms/<REALM_NAME>
# the client id for the 'client' application
client-id: <CLIENT_ID>
# the secret associated to the 'client' application
client-secret: <CLIENT_SECRET>
# the interface definition you wish the proxy to listen, all interfaces is specified as ':<port>', unix sockets as unix://<REL_PATH>|</ABS PATH>
listen: 127.0.0.1:3000
# whether to enable refresh tokens
enable-refresh-tokens: true
# the location of a certificate you wish the proxy to use for TLS support
tls-cert:
# the location of a private key for TLS
tls-private-key:
# the redirection url, essentially the site url, note: /oauth/callback is added at the end
redirection-url: http://127.0.0.1:3000
# the encryption key used to encode the session state
encryption-key: <ENCRYPTION_KEY>
# the upstream endpoint which we should proxy request
upstream-url: http://127.0.0.1:80
# additional scopes to add to add to the default (openid+email+profile)
scopes:
- vpn-user
# a collection of resource i.e. urls that you wish to protect
resources:
- uri: /admin/test
  # the methods on this url that should be protected, if missing, we assuming all
  methods:
  - GET
  # a list of roles the user must have in order to access urls under the above
  # If all you want is authentication ONLY, simply remove the roles array - the user must be authenticated but
  # no roles are required
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

#### 2.4.3. Example usage and configuration {#Example_usage_and_configuration}
Assuming you have some web service you wish protected by Keycloak:

- Create the **client** using the Keycloak GUI or CLI; the client protocol is **'openid-connect'**, access-type: **confidential**.
- Add a Valid Redirect URI of **http://127.0.0.1:3000/oauth/callback**.
- Grab the client id and client secret.
- Create the various roles under the client or existing clients for authorization purposes.

Here is an example configuration file.

```
client-id: <CLIENT_ID>
client-secret: <CLIENT_SECRET> # require for access_type: confidential
# Note the redirection-url is optional, it will default to the X-Forwarded-Proto / X-Forwarded-Host r the URL scheme and host not found
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

#### 2.4.4. OpenID Provider Communication {#OpenID_Provider_Communication}
By default the communication with the OpenID provider is direct. If you wish, you can specify a forwarding proxy server in your configuration file:

```
openid-provider-proxy: http://proxy.example.com:8080
```

#### 2.4.5. HTTP routing {#HTTP_routing}
By default all requests will be proxyed on to the upstream, if you wish to ensure all requests are authentication you can use this:

```
--resource=uri=/* # note, unless specified the method is assumed to be 'any|ANY'
```

The HTTP routing rules follow the guidelines from [chi](https://github.com/go-chi/chi#router-design). The ordering of the resources do not matter, the router will handle that for you.

#### 2.4.6. Session-only cookies {#Session-only_cookies}
By default the access and refresh cookies are session-only and disposed of on browser close; you can disable this feature using the `--enable-session-cookies` option.

#### 2.4.7. Forward-signing proxy {#Forward-signing_proxy}
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
  # Note: if you don't specify any forwarding domains, all domains will be signed; Also the code checks is the
  # domain 'contains' the value (it's not a regex) so if you wanted to sign all requests to svc.cluster.local, just use
  # svc.cluster.local
  volumeMounts:
  - name: keycloak-socket
    mountPoint: /var/run/keycloak
- name: projecta
  image: some_images

# test the forward proxy
$ curl -k --proxy http://127.0.0.1:3000 https://test.projesta.svc.cluster.local
```

On the receiver side you could set up the Keycloak Gatekeeper (--no=redirects=true) and permit this to verify and handle admission for you. Alternatively, the access token can found as a bearer token in the request.

#### 2.4.8. Forwarding signed HTTPS connections {#Forwarding_signed_HTTPS_connections}
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

#### 2.4.9. HTTPS redirect {#HTTPS_redirect}
The proxy supports an HTTP listener, so the only real requirement here is to perform an HTTP → HTTPS redirect. You can enable the option like this:

```
--listen-http=127.0.0.1:80
--enable-security-filter=true  # is required for the https redirect
--enable-https-redirection
```

#### 2.4.10. Let’s Encrypt configuration {#Let’s_Encrypt_configuration}
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

#### 2.4.11. Access token encryption {#Access_token_encryption}
By default, the session token is placed into a cookie in plaintext. If you prefer to encrypt the session cookie, use the `--enable-encrypted-token` and `--encryption-key` options. Note that the access token forwarded in the X-Auth-Token header to upstream is unaffected.

#### 2.4.12. Upstream headers {#Upstream_headers}
On protected resources, the upstream endpoint will receive a number of headers added by the proxy, along with custom claims, like this:

```
# add the header to the upstream endpoint
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

#### 2.4.13. Custom claim headers {#Custom_claim_headers}
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

#### 2.4.14. Custom headers {#Custom_headers}
You can inject custom headers using the `--headers="name=value"` option or the configuration file:

```
headers:
  name: value
```

#### 2.4.15. Encryption key {#Encryption_key}
In order to remain stateless and not have to rely on a central cache to persist the refresh_tokens, the refresh token is encrypted and added as a cookie using **crypto/aes**. The key must be the same if you are running behind a load balancer. The key length should be either 16 or 32 bytes, depending or whether you want AES-128 or AES-256.

#### 2.4.16. Claim matching {#Claim_matching}
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

#### 2.4.17. Group claims {#Group_claims}
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

#### 2.4.18. Custom pages {#Custom_pages}
By default, Keycloak Gatekeeper will immediately redirect you for authentication and hand back a 403 for access denied. Most users will probably want to present the user with a more friendly sign-in and access denied page. You can pass the command line options (or via config file) paths to the files with `--signin-page=PATH`. The sign-in page will have a 'redirect' variable passed into the scope and holding the oauth redirection url. If you wish to pass additional variables into the templates, such as title, sitename and so on, you can use the -`-tags key=pair` option, like this: `--tags title="This is my site"` and the variable would be accessible from `{{ title }}`.

```
<html>
<body>
<a href="{{ redirect }}">Sign-in</a>
</body>
</html>
```

#### 2.4.19. White-listed URL’s {#White-listed_URL’s}
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

#### 2.4.20. Mutual TLS {#Mutual_TLS}
The proxy support enforcing mutual TLS for the clients by adding the `--tls-ca-certificate` command line option or configuration file option. All clients connecting must present a certificate which was signed by the CA being used.

#### 2.4.21. Certificate rotation {#Certificate_rotation}
The proxy will automatically rotate the server certificates if the files change on disk. Note, no down time will occur as the change is made inline. Clients who connected prior to the certificate rotation will be unaffected and will continue as normal with all new connections presented with the new certificate.

#### 2.4.22. Refresh tokens {#Refresh_tokens}
If a request for an access token contains a refresh token and `--enable-refresh-tokens` is set to `true`, the proxy will automatically refresh the access token for you. The tokens themselves are kept either as an encrypted **(--encryption-key=KEY)** cookie **(cookie name: kc-state).** or a store **(still requires encryption key)**.

At present the only store options supported are [Redis](https://github.com/antirez/redis) and [Boltdb](https://github.com/boltdb/bolt).

To enable a local boltdb store use `--store-url boltdb:///PATH` or using a relative path `boltdb://PATH`.

To enable a local redis store use `redis://[USER:PASSWORD@]HOST:PORT`. In both cases the refresh token is encrypted before being placed into the store.

#### 2.4.23. Logout endpoint {#Logout_endpoint}
A **/oauth/logout?redirect=url** is provided as a helper to log users out. In addition to dropping any session cookies, we also attempt to revoke access via revocation url (config **revocation-url** or **--revocation-url**) with the provider. For Keycloak, the url for this would be <https://keycloak.example.com/auth/realms/REALM_NAME/protocol/openid-connect/logout>. If the url is not specified we will attempt to grab the url from the OpenID discovery response.

#### 2.4.24. Cross-origin resource sharing (CORS) {#Cross-origin_resource_sharing__CORS_}
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

#### 2.4.25. Upstream URL {#Upstream_URL}
You can control the upstream endpoint via the `--upstream-url` option. Both HTTP and HTTPS are supported with TLS verification and keep-alive support configured via the `--skip-upstream-tls-verify` / `--upstream-keepalives` option. Note, the proxy can also upstream via a UNIX socket, `--upstream-url unix://path/to/the/file.sock`.

#### 2.4.26. Endpoints {#Endpoints}
- **/oauth/authorize** is authentication endpoint which will generate the OpenID redirect to the provider
- **/oauth/callback** is provider OpenID callback endpoint
- **/oauth/expired** is a helper endpoint to check if a access token has expired, 200 for ok and, 401 for no token and 401 for expired
- **/oauth/health** is the health checking endpoint for the proxy, you can also grab version from headers
- **/oauth/login** provides a relay endpoint to login via `grant_type=password`, for example, `POST /oauth/login` form values are `username=USERNAME&password=PASSWORD` (must be enabled)
- **/oauth/logout** provides a convenient endpoint to log the user out, it will always attempt to perform a back channel log out of offline tokens
- **/oauth/token** is a helper endpoint which will display the current access token for you
- **/oauth/metrics** is a Prometheus metrics handler

#### 2.4.27. Metrics {#Metrics}
Assuming `--enable-metrics` has been set, a Prometheus endpoint can be found on **/oauth/metrics**; at present the only metric being exposed is a counter per HTTP code.

#### 2.4.28. Limitations {#Limitations}
Keep in mind [browser cookie limits](http://browsercookielimits.squawky.net/) if you use access or refresh tokens in the browser cookie. Keycloak-generic-adapter divides the cookie automatically if your cookie is longer than 4093 bytes. Real size of the cookie depends on the content of the issued access token. Also, encryption might add additional bytes to the cookie size. If you have large cookies (>200 KB), you might reach browser cookie limits.

All cookies are part of the header request, so you might find a problem with the max headers size limits in your infrastructure (some load balancers have very low this value, such as 8 KB). Be sure that all network devices have sufficient header size limits. Otherwise, your users won’t be able to obtain an access token.

#### 2.4.29. Known Issues {#Known_Issues}
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

    # maps the prefered_username claim to the REMOTE_USER environment variable
    OIDCRemoteUserClaim preferred_username

    <Location /${CLIENT_APP_NAME}/>
        AuthType openid-connect
        Require valid-user
    </Location>
</VirtualHost>
```

Further information on how to configure mod_auth_openidc can be found on the [mod_auth_openidc](https://github.com/zmartzone/mod_auth_openidc) project page.

### 2.5. 其他OpenID连接库 {#Other_OpenID_Connect_Libraries}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/oidc/oidc-generic.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/oidc/oidc-generic.adoc)

Keycloak can be secured by supplied adapters that are usually easier to use and provide better integration with Keycloak. However, if an adapter is not available for your programming language, framework, or platform you might opt to use a generic OpenID Connect Resource Provider (RP) library instead. This chapter describes details specific to Keycloak and does not contain specific protocol details. For more information see the [OpenID Connect specifications](https://openid.net/connect/) and [OAuth2 specification](https://tools.ietf.org/html/rfc6749).

#### 2.5.1. Endpoints {#Endpoints}
The most important endpoint to understand is the `well-known` configuration endpoint. It lists endpoints and other configuration options relevant to the OpenID Connect implementation in Keycloak. The endpoint is:

```
/realms/{realm-name}/.well-known/openid-configuration
```

To obtain the full URL, add the base URL for Keycloak and replace `{realm-name}` with the name of your realm. For example:

http://localhost:8080/auth/realms/master/.well-known/openid-configuration

Some RP libraries retrieve all required endpoints from this endpoint, but for others you might need to list the endpoints individually.

##### Authorization Endpoint {#Authorization_Endpoint}
```
/realms/{realm-name}/protocol/openid-connect/auth
```

The authorization endpoint performs authentication of the end-user. This is done by redirecting the user agent to this endpoint.

For more details see the [Authorization Endpoint](https://openid.net/specs/openid-connect-core-1_0.html#AuthorizationEndpoint) section in the OpenID Connect specification.

##### Token Endpoint {#Token_Endpoint}
```
/realms/{realm-name}/protocol/openid-connect/token
```

The token endpoint is used to obtain tokens. Tokens can either be obtained by exchanging an authorization code or by supplying credentials directly depending on what flow is used. The token endpoint is also used to obtain new access tokens when they expire.

For more details see the [Token Endpoint](https://openid.net/specs/openid-connect-core-1_0.html#TokenEndpoint) section in the OpenID Connect specification.

##### Userinfo Endpoint {#Userinfo_Endpoint}
```
/realms/{realm-name}/protocol/openid-connect/userinfo
```

The userinfo endpoint returns standard claims about the authenticated user, and is protected by a bearer token.

For more details see the [Userinfo Endpoint](https://openid.net/specs/openid-connect-core-1_0.html#UserInfo) section in the OpenID Connect specification.

##### Logout Endpoint {#Logout_Endpoint}
```
/realms/{realm-name}/protocol/openid-connect/logout
```

The logout endpoint logs out the authenticated user.

The user agent can be redirected to the endpoint, in which case the active user session is logged out. Afterward the user agent is redirected back to the application.

The endpoint can also be invoked directly by the application. To invoke this endpoint directly the refresh token needs to be included as well as the credentials required to authenticate the client.

##### Certificate Endpoint {#Certificate_Endpoint}
```
/realms/{realm-name}/protocol/openid-connect/certs
```

The certificate endpoint returns the public keys enabled by the realm, encoded as a JSON Web Key (JWK). Depending on the realm settings there can be one or more keys enabled for verifying tokens. For more information see the [Server Administration Guide](https://www.keycloak.org/docs/6.0/server_admin/) and the [JSON Web Key specification](https://tools.ietf.org/html/rfc7517).

##### Introspection Endpoint {#Introspection_Endpoint}
```
/realms/{realm-name}/protocol/openid-connect/token/introspect
```

The introspection endpoint is used to retrieve the active state of a token. In other words, you can use it to validate an access or refresh token. It can only be invoked by confidential clients.

For more details on how to invoke on this endpoint, see [OAuth 2.0 Token Introspection specification](https://tools.ietf.org/html/rfc7662).

##### Dynamic Client Registration Endpoint {#Dynamic_Client_Registration_Endpoint}
```
/realms/{realm-name}/clients-registrations/openid-connect
```

The dynamic client registration endpoint is used to dynamically register clients.

For more details see the [Client Registration chapter](https://www.keycloak.org/docs/latest/securing_apps/index.html#_client_registration) and the [OpenID Connect Dynamic Client Registration specification](https://openid.net/specs/openid-connect-registration-1_0.html).

#### 2.5.2. Validating Access Tokens {#Validating_Access_Tokens}
If you need to manually validate access tokens issued by Keycloak you can invoke the [Introspection Endpoint](https://www.keycloak.org/docs/latest/securing_apps/index.html#_token_introspection_endpoint). The downside to this approach is that you have to make a network invocation to the Keycloak server. This can be slow and possibily overload the server if you have too many validation requests going on at the same time. Keycloak issued access tokens are [JSON Web Tokens (JWT)](https://tools.ietf.org/html/rfc7519) digitally signed and encoded using [JSON Web Signature (JWS)](https://www.rfc-editor.org/rfc/rfc7515.txt). Because they are encoded in this way, this allows you to locally validate access tokens using the public key of the issuing realm. You can either hard code the realm’s public key in your validation code, or lookup and cache the public key using the [certificate endpoint](https://www.keycloak.org/docs/latest/securing_apps/index.html#_certificate_endpoint) with the Key ID (KID) embedded within the JWS. Depending what language you code in, there are a multitude of third party libraries out there that can help you with JWS validation.

#### 2.5.3. Flows {#Flows}
##### Authorization Code {#Authorization_Code}
The Authorization Code flow redirects the user agent to Keycloak. Once the user has successfully authenticated with Keycloak an Authorization Code is created and the user agent is redirected back to the application. The application then uses the authorization code along with its credentials to obtain an Access Token, Refresh Token and ID Token from Keycloak.

The flow is targeted towards web applications, but is also recommended for native applications, including mobile applications, where it is possible to embed a user agent.

For more details refer to the [Authorization Code Flow](https://openid.net/specs/openid-connect-core-1_0.html#CodeFlowAuth) in the OpenID Connect specification.

##### Implicit {#Implicit}
The Implicit flow redirects works similarly to the Authorization Code flow, but instead of returning an Authorization Code the Access Token and ID Token is returned. This reduces the need for the extra invocation to exchange the Authorization Code for an Access Token. However, it does not include a Refresh Token. This results in the need to either permit Access Tokens with a long expiration, which is problematic as it’s very hard to invalidate these. Or requires a new redirect to obtain new Access Token once the initial Access Token has expired. The Implicit flow is useful if the application only wants to authenticate the user and deals with logout itself.

There’s also a Hybrid flow where both the Access Token and an Authorization Code is returned.

One thing to note is that both the Implicit flow and Hybrid flow has potential security risks as the Access Token may be leaked through web server logs and browser history. This is somewhat mitigated by using short expiration for Access Tokens.

For more details refer to the [Implicit Flow](https://openid.net/specs/openid-connect-core-1_0.html#ImplicitFlowAuth) in the OpenID Connect specification.

##### Resource Owner Password Credentials {#Resource_Owner_Password_Credentials}
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

###### Example using CURL {#Example_using_CURL}
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

##### Client Credentials {#Client_Credentials}
Client Credentials is used when clients (applications and services) wants to obtain access on behalf of themselves rather than on behalf of a user. This can for example be useful for background services that applies changes to the system in general rather than for a specific user.

Keycloak provides support for clients to authenticate either with a secret or with public/private keys.

This flow is not included in OpenID Connect, but is a part of the OAuth 2.0 specification.

For more details refer to the [Client Credentials Grant](https://tools.ietf.org/html/rfc6749#section-4.4) chapter in the OAuth 2.0 specification.

#### 2.5.4. Redirect URIs {#Redirect_URIs}
When using the redirect based flows it’s important to use valid redirect uris for your clients. The redirect uris should be as specific as possible. This especially applies to client-side (public clients) applications. Failing to do so could result in:

- Open redirects - this can allow attackers to create spoof links that looks like they are coming from your domain
- Unauthorized entry - when users are already authenticated with Keycloak an attacker can use a public client where redirect uris have not be configured correctly to gain access by redirecting the user without the users knowledge

In production for web applications always use `https` for all redirect URIs. Do not allow redirects to http.

There’s also a few special redirect URIs:

- `http://localhost`

  This redirect URI is useful for native applications and allows the native application to create a web server on a random port that can be used to obtain the authorization code. This redirect uri allows any port.

- `urn:ietf:wg:oauth:2.0:oob`

  If its not possible to start a web server in the client (or a browser is not available) it is possible to use the special `urn:ietf:wg:oauth:2.0:oob` redirect uri. When this redirect uri is used Keycloak displays a page with the code in the title and in a box on the page. The application can either detect that the browser title has changed, or the user can copy/paste the code manually to the application. With this redirect uri it is also possible for a user to use a different device to obtain a code to paste back to the application.

## 3. SAML {#SAML}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/saml-overview.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/saml-overview.adoc)

This section describes how you can secure applications and services with SAML using either Keycloak client adapters or generic SAML provider libraries.

### 3.1. Java 适配器 {#Java_Adapters}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/java-adapters.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/java-adapters.adoc)

Keycloak comes with a range of different adapters for Java application. Selecting the correct adapter depends on the target platform.

#### 3.1.1. General Adapter Config {#General_Adapter_Config}

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

##### SP Element {#SP_Element}
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

##### Service Provider Keys and Key Elements {#Service_Provider_Keys_and_Key_Elements}
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

###### KeyStore element {#KeyStore_element}
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

###### Key PEMS {#Key_PEMS}
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

##### SP PrincipalNameMapping element {#SP_PrincipalNameMapping_element}
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

##### RoleIdentifiers Element {#RoleIdentifiers_Element}
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

##### IDP Element {#IDP_Element}
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

##### IDP SingleSignOnService sub element {#IDP_SingleSignOnService_sub_element}
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

##### IDP SingleLogoutService sub element {#IDP_SingleLogoutService_sub_element}
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

##### IDP Keys sub element {#IDP_Keys_sub_element}
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

##### IDP HttpClient sub element {#IDP_HttpClient_sub_element}
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

#### 3.1.2. JBoss EAP/WildFly Adapter {# JBoss_EAP_WildFly_Adapter}

To be able to secure WAR apps deployed on JBoss EAP or WildFly, you must install and configure the Keycloak SAML Adapter Subsystem.

You then provide a keycloak config, `/WEB-INF/keycloak-saml.xml` file in your WAR and change the auth-method to KEYCLOAK-SAML within web.xml. Both methods are described in this section.

##### Adapter Installation {#Adapter_Installation}
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

##### JBoss SSO {#JBoss_SSO}
WildFly has built-in support for single sign-on for web applications deployed to the same WildFly instance. This should not be enabled when using Keycloak.

#### 3.1.3. Installing JBoss EAP Adapter from an RPM {#Installing_JBoss_EAP_Adapter_from_an_RPM}

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

##### Per WAR Configuration {#Per_WAR_Configuration}
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

##### Securing WARs via Keycloak SAML Subsystem {#Securing_WARs_via_Keycloak_SAML_Subsystem}
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

#### 3.1.4. Tomcat SAML adapters {#Tomcat_SAML_adapters}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/tomcat-adapter.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/tomcat-adapter.adoc)

To be able to secure WAR apps deployed on Tomcat 6, 7 and 8 you must install the Keycloak Tomcat 6, 7 or 8 SAML adapter into your Tomcat installation. You then have to provide some extra configuration in each WAR you deploy to Tomcat. Let’s go over these steps.

##### Adapter Installation {#Adapter_Installation}
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

##### Per WAR Configuration {#Per_WAR_Configuration}
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

#### 3.1.5. Jetty SAML Adapters {#Jetty_SAML_Adapters}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/jetty-adapter.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/jetty-adapter.adoc)

To be able to secure WAR apps deployed on Jetty you must install the Keycloak Jetty 9.x SAML adapter into your Jetty installation. You then have to provide some extra configuration in each WAR you deploy to Jetty. Let’s go over these steps.

##### Jetty 9 Adapter Installation {#Jetty_9_Adapter_Installation}
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

##### Jetty 9 Per WAR Configuration {#Jetty_9_Per_WAR_Configuration}
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

#### 3.1.6. Java Servlet Filter Adapter {#Java_Servlet_Filter_Adapter}
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

#### 3.1.7. Registering with an Identity Provider {#Registering_with_an_Identity_Provider}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/idp-registration.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/idp-registration.adoc)

For each servlet-based adapter, the endpoint you register for the assert consumer service URL and single logout service must be the base URL of your servlet application with `/saml` appended to it, that is, `https://example.com/contextPath/saml`.

#### 3.1.8. Logout {#Logout}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/logout.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/logout.adoc)

There are multiple ways you can logout from a web application. For Java EE servlet containers, you can call `HttpServletRequest.logout()`. For any other browser application, you can point the browser at any url of your web application that has a security constraint and pass in a query parameter GLO, i.e. `http://myapp?GLO=true`. This will log you out if you have an SSO session with your browser.

##### Logout in Clustered Environment {#Logout_in_Clustered_Environment}
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

##### Logout in Cross DC Scenario {#Logout_in_Cross_DC_Scenario}
The cross DC scenario only applies to WildFly 10 and higher, and EAP 7 and higher.

Special handling is needed for handling sessions that span multiple data centers. Imagine the following scenario:

1. Login requests are handled within cluster in data center 1.
2. Admin issues logout request for a particular SAML session, the request lands in data center 2.

The data center 2 has to log out all sessions that are present in data center 1 (and all other data centers that share HTTP sessions).

To cover this case, the SAML session cache described [above](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml_logout_in_cluster) needs to be replicated not only within individual clusters but across all the data centers e.g. [via standalone Infinispan/JDG server](https://access.redhat.com/documentation/en-us/red_hat_data_grid/6.6/html/administration_and_configuration_guide/chap-externalize_sessions#Externalize_HTTP_Session_from_JBoss_EAP_6.x_to_JBoss_Data_Grid):

1. A cache has to be added to the standalone Infinispan/JDG server.
2. The cache from previous item has to be added as a remote store for the respective SAML session cache.

Once remote store is found to be present on SAML session cache during deployment, it is watched for changes and the local SAML session cache is updated accordingly.

#### 3.1.9. Obtaining Assertion Attributes {#Obtaining_Assertion_Attributes}
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

#### 3.1.10. Error Handling {#Error_Handling}
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

#### 3.1.11. Troubleshooting {#Troubleshooting}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/debugging.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/debugging.adoc)

The best way to troubleshoot problems is to turn on debugging for SAML in both the client adapter and Keycloak Server. Using your logging framework, set the log level to `DEBUG` for the `org.keycloak.saml`package. Turning this on allows you to see the SAML requests and response documents being sent to and from the server.

#### 3.1.12. Multi Tenancy {#Multi_Tenancy}
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

#### 3.1.13. Migration from older versions {#Migration_from_older_versions}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/saml/java/MigrationFromOlderVersions.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/saml/java/MigrationFromOlderVersions.adoc)

##### Migrating to 1.9.0 {#Migrating_to_1_9_0}
###### SAML SP Client Adapter Changes {#SAML_SP_Client_Adapter_Changes}
Keycloak SAML SP Client Adapter now requires a specific endpoint, `/saml` to be registered with your IdP. The SamlFilter must also be bound to /saml in addition to any other binding it has. This had to be done because SAML POST binding would eat the request input stream and this would be really bad for clients that relied on it.

### 3.2. mod_auth_mellon Apache HTTPD 模块 {#mod_auth_mellon_Apache_HTTPD_module}
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

#### 3.2.1. Configuring mod_auth_mellon with Keycloak {#Configuring_mod}
There are two hosts involved:

- The host on which Keycloak is running, which will be referred to as $idp_host because Keycloak is a SAML identity provider (IdP).
- The host on which the web application is running, which will be referred to as $sp_host. In SAML an application using an IdP is called a service provider (SP).

All of the following steps need to performed on $sp_host with root privileges.

##### Installing the Packages {#Installing_the_Packages}
To install the necessary packages, you will need:

- Apache Web Server (httpd)
- Mellon SAML SP add-on module for Apache
- Tools to create X509 certificates

To install the necessary packages, run this command:

```
yum install httpd mod_auth_mellon mod_ssl openssl
```

##### Creating a Configuration Directory for Apache SAML {#Creating_a_Configuration_Directory_for_Apache_SAML}
It is advisable to keep configuration files related to Apache’s use of SAML in one location.

Create a new directory named saml2 located under the Apache configuration root /etc/httpd:

```
mkdir /etc/httpd/saml2
```

##### Configuring the Mellon Service Provider {#Configuring_the_Mellon_Service_Provider}
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

##### Creating the Service Provider Metadata {#Creating_the_Service_Provider_Metadata}
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

##### Adding the Mellon Service Provider to the Keycloak Identity Provider {#Adding_the_Mellon_Service_Provider_to_the_Keycloak_Identity_Provider}
Assumption: The Keycloak IdP has already been installed on the $idp_host.

Keycloak supports multiple tenancy where all users, clients, and so on are grouped in what is called a realm. Each realm is independent of other realms. You can use an existing realm in your Keycloak, but this example shows how to create a new realm called test_realm and use that realm.

All these operations are performed using the Keycloak administration web console. You must have the admin username and password for $idp_host.

To complete the following steps:

1. Open the Admin Console and log on by entering the admin username and password.

   After logging into the administration console there will be an existing realm. When Keycloak is first set up a root realm, master, is created by default. Any previously created realms are listed in the upper left corner of the administration console in a drop-down list.

2. From the realm drop-down list select **Add realm**.

3. In the Name field type `test_realm` and click **Create**.

###### Adding the Mellon Service Provider as a Client of the Realm {#Adding_the_Mellon_Service_Provider_as_a_Client_of_the_Realm}
In Keycloak SAML SPs are known as clients. To add the SP we must be in the Clients section of the realm.

1. Click the Clients menu item on the left and click **Create** in the upper right corner to create a new client.

###### Adding the Mellon SP Client {#Adding_the_Mellon_SP_Client}
To add the Mellon SP client, complete the following steps:

1. Set the client protocol to SAML. From the Client Protocol drop down list, select **saml**.
2. Provide the Mellon SP metadata file created above (/etc/httpd/saml2/mellon_metadata.xml). Depending on where your browser is running you might have to copy the SP metadata from $sp_host to the machine on which your browser is running so the browser can find the file.
3. Click **Save**.

###### Editing the Mellon SP Client {#Editing_the_Mellon_SP_Client}
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

###### Retrieving the Identity Provider Metadata {#Retrieving_the_Identity_Provider_Metadata}
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

## 4. Docker注册表配置 {#Docker_Registry_Configuration}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/docker/docker-overview.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/docker/docker-overview.adoc)

|      | Docker authentication is disabled by default. To enable see [Profiles](https://www.keycloak.org/docs/6.0/server_installation/#profiles). |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

This section describes how you can configure a Docker registry to use Keycloak as its authentication server.

For more information on how to set up and configure a Docker registry, see the [Docker Registry Configuration Guide](https://docs.docker.com/registry/configuration/).

### 4.1. Docker注册表配置文件安装 {#Docker_Registry_Configuration_File_Installation}
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

### 4.2. Docker注册表环境变量覆盖安装 {#Docker_Registry_Environment_Variable_Override_Installation}
Often times it is appropriate to use a simple environment variable override for develop or POC Docker registries. While this approach is usually not recommended for production use, it can be helpful when one requires quick-and-dirty way to stand up a registry. Simply use the *Variable Override* Format Option from the client installation tab, and an output should appear like the one below:

```
REGISTRY_AUTH_TOKEN_REALM: http://localhost:8080/auth/realms/master/protocol/docker-v2/auth
REGISTRY_AUTH_TOKEN_SERVICE: docker-test
REGISTRY_AUTH_TOKEN_ISSUER: http://localhost:8080/auth/realms/master
```

|      | Don’t forget to configure the `REGISTRY_AUTH_TOKEN_ROOTCERTBUNDLE` override with the location of the Keycloak realm’s pulic certificate. The auth configuration will not work without this argument. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 4.3. Docker撰写YAML文件 {#Docker_Compose_YAML_File}
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

## 5. 客户端注册 {#Client_Registration}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/client-registration.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/client-registration.adoc)

In order for an application or service to utilize Keycloak it has to register a client in Keycloak. An admin can do this through the admin console (or admin REST endpoints), but clients can also register themselves through the Keycloak client registration service.

The Client Registration Service provides built-in support for Keycloak Client Representations, OpenID Connect Client Meta Data and SAML Entity Descriptors. The Client Registration Service endpoint is `/auth/realms/<realm>/clients-registrations/<provider>`.

The built-in supported `providers` are:

- default - Keycloak Client Representation (JSON)
- install - Keycloak Adapter Configuration (JSON)
- openid-connect - OpenID Connect Client Metadata Description (JSON)
- saml2-entity-descriptor - SAML Entity Descriptor (XML)

The following sections will describe how to use the different providers.

### 5.1. 认证 {#Authentication}
To invoke the Client Registration Services you usually need a token. The token can be a bearer token, an initial access token or a registration access token. There is an alternative to register new client without any token as well, but then you need to configure Client Registration Policies (see below).

#### 5.1.1. Bearer Token {#Bearer_Token}
The bearer token can be issued on behalf of a user or a Service Account. The following permissions are required to invoke the endpoints (see [Server Administration Guide](https://www.keycloak.org/docs/6.0/server_admin/) for more details):

- create-client or manage-client - To create clients
- view-client or manage-client - To view clients
- manage-client - To update or delete client

If you are using a bearer token to create clients it’s recommend to use a token from a Service Account with only the `create-client` role (see [Server Administration Guide](https://www.keycloak.org/docs/6.0/server_admin/) for more details).

#### 5.1.2. Initial Access Token {#Initial_Access_Token}
The recommended approach to registering new clients is by using initial access tokens. An initial access token can only be used to create clients and has a configurable expiration as well as a configurable limit on how many clients can be created.

An initial access token can be created through the admin console. To create a new initial access token first select the realm in the admin console, then click on `Realm Settings` in the menu on the left, followed by `Client Registration` in the tabs displayed in the page. Then finally click on `Initial Access Tokens` sub-tab.

You will now be able to see any existing initial access tokens. If you have access you can delete tokens that are no longer required. You can only retrieve the value of the token when you are creating it. To create a new token click on `Create`. You can now optionally add how long the token should be valid, also how many clients can be created using the token. After you click on `Save` the token value is displayed.

It is important that you copy/paste this token now as you won’t be able to retrieve it later. If you forget to copy/paste it, then delete the token and create another one.

The token value is used as a standard bearer token when invoking the Client Registration Services, by adding it to the Authorization header in the request. For example:

```
Authorization: bearer eyJhbGciOiJSUz...
```

#### 5.1.3. Registration Access Token {#Registration_Access_Token}
When you create a client through the Client Registration Service the response will include a registration access token. The registration access token provides access to retrieve the client configuration later, but also to update or delete the client. The registration access token is included with the request in the same way as a bearer token or initial access token. Registration access tokens are only valid once, when it’s used the response will include a new token.

If a client was created outside of the Client Registration Service it won’t have a registration access token associated with it. You can create one through the admin console. This can also be useful if you loose the token for a particular client. To create a new token find the client in the admin console and click on `Credentials`. Then click on `Generate registration access token`.

### 5.2. Keycloak表示 {#Keycloak_Representations}
The `default` client registration provider can be used to create, retrieve, update and delete a client. It uses Keycloak Client Representation format which provides support for configuring clients exactly as they can be configured through the admin console, including for example configuring protocol mappers.

To create a client create a Client Representation (JSON) then perform an HTTP POST request to `/auth/realms/<realm>/clients-registrations/default`.

It will return a Client Representation that also includes the registration access token. You should save the registration access token somewhere if you want to retrieve the config, update or delete the client later.

To retrieve the Client Representation perform an HTTP GET request to `/auth/realms/<realm>/clients-registrations/default/<client id>`.

It will also return a new registration access token.

To update the Client Representation perform an HTTP PUT request with the updated Client Representation to:`/auth/realms/<realm>/clients-registrations/default/<client id>`.

It will also return a new registration access token.

To delete the Client Representation perform an HTTP DELETE request to: `/auth/realms/<realm>/clients-registrations/default/<client id>`

### 5.3. Keycloak适配器配置 {#Keycloak_Adapter_Configuration}
The `installation` client registration provider can be used to retrieve the adapter configuration for a client. In addition to token authentication you can also authenticate with client credentials using HTTP basic authentication. To do this include the following header in the request:

```
Authorization: basic BASE64(client-id + ':' + client-secret)
```

To retrieve the Adapter Configuration then perform an HTTP GET request to `/auth/realms/<realm>/clients-registrations/install/<client id>`.

No authentication is required for public clients. This means that for the JavaScript adapter you can load the client configuration directly from Keycloak using the above URL.

### 5.4. OpenID连接动态客户端注册 {#OpenID_Connect_Dynamic_Client_Registration}
Keycloak implements [OpenID Connect Dynamic Client Registration](https://openid.net/specs/openid-connect-registration-1_0.html), which extends [OAuth 2.0 Dynamic Client Registration Protocol](https://tools.ietf.org/html/rfc7591) and [OAuth 2.0 Dynamic Client Registration Management Protocol](https://tools.ietf.org/html/rfc7592).

The endpoint to use these specifications to register clients in Keycloak is `/auth/realms/<realm>/clients-registrations/openid-connect[/<client id>]`.

This endpoint can also be found in the OpenID Connect Discovery endpoint for the realm, `/auth/realms/<realm>/.well-known/openid-configuration`.

### 5.5. SAML实体描述符 {#SAML_Entity_Descriptors}
The SAML Entity Descriptor endpoint only supports using SAML v2 Entity Descriptors to create clients. It doesn’t support retrieving, updating or deleting clients. For those operations the Keycloak representation endpoints should be used. When creating a client a Keycloak Client Representation is returned with details about the created client, including a registration access token.

To create a client perform an HTTP POST request with the SAML Entity Descriptor to `/auth/realms/<realm>/clients-registrations/saml2-entity-descriptor`.

### 5.6. 使用CURL的示例 {#Example_using_CURL}
The following example creates a client with the clientId `myclient` using CURL. You need to replace `eyJhbGciOiJSUz…` with a proper initial access token or bearer token.

```
curl -X POST \
    -d '{ "clientId": "myclient" }' \
    -H "Content-Type:application/json" \
    -H "Authorization: bearer eyJhbGciOiJSUz..." \
    http://localhost:8080/auth/realms/master/clients-registrations/default
```

### 5.7. 使用Java客户端注册API的示例 {#Example_using_Java_Client_Registration_API}
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

### 5.8. 客户端注册政策 {#Client_Registration_Policies}
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

## 6. 客户端注册 CLI {#Client_Registration_CLI}

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/securing_apps/topics/client-registration/client-registration-cli.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: securing_apps/topics/client-registration/client-registration-cli.adoc)

The Client Registration CLI is a command-line interface (CLI) tool for application developers to configure new clients in a self-service manner when integrating with Keycloak. It is specifically designed to interact with Keycloak Client Registration REST endpoints.

It is necessary to create or obtain a client configuration for any application to be able to use Keycloak. You usually configure a new client for each new application hosted on a unique host name. When an application interacts with Keycloak, the application identifies itself with a client ID so Keycloak can provide a login page, single sign-on (SSO) session management, and other services.

You can configure application clients from a command line with the Client Registration CLI, and you can use it in shell scripts.

To allow a particular user to use `Client Registration CLI` the Keycloak administrator typically uses the Admin Console to configure a new user with proper roles or to configure a new client and client secret to grant access to the Client Registration REST API.

### 6.1. 配置新常规用户以使用客户端注册CLI {#Configuring_a_new_regular_user_for_use_with_Client_Registration_CLI}
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

### 6.2. 配置客户端以与客户端注册CLI一起使用 {#Configuring_a_client_for_use_with_the_Client_Registration_CLI}
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

### 6.3. 安装客户端注册CLI {#Installing_the_Client_Registration_CLI}
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

### 6.4. 使用客户端注册CLI {#Using_the_Client_Registration_CLI}
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

#### 6.4.1. Logging in {#Logging_in}
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

#### 6.4.2. Working with alternative configurations {#Working_with_alternative_configurations}
By default, the Client Registration CLI automatically maintains a configuration file at a default location, `./.keycloak/kcreg.config`, under the user’s home directory. You can use the `--config` option to point to a different file or location to mantain multiple authenticated sessions in parallel. It is the safest way to perform operations tied to a single configuration file from a single thread.

|      | Do not make the configuration file visible to other users on the system. The configuration file contains access tokens and secrets that should be kept private. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

You might want to avoid storing secrets inside a configuration file by using the `--no-config` option with all of your commands, even though it is less convenient and requires more token requests to do so. Specify all authentication information with each `kcreg` invocation.

#### 6.4.3. Initial Access and Registration Access Tokens {#Initial_Access_and_Registration_Access_Tokens}
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

#### 6.4.4. Creating a client configuration {#Creating_a_client_configuration}
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

#### 6.4.5. Retrieving a client configuration {#Retrieving_a_client_configuration}
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

#### 6.4.6. Modifying a client configuration {#Modifying_a_client_configuration}
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

#### 6.4.7. Deleting a client configuration {#Deleting_a_client_configuration}
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

#### 6.4.8. Refreshing invalid Registration Access Tokens {#Refreshing_invalid_Registration_Access_Tokens}
When performing a create, read, update, and delete (CRUD) operation using the `--no-config` mode, the Client Registration CLI cannot handle Registration Access Tokens for you. In that case, it is possible to lose track of the most recently issued Registration Access Token for a client, which makes it impossible to perform any further CRUD operations on that client without authenticating with an account that has **manage-clients** permissions.

If you have permissions, you can issue a new Registration Access Token for the client and have it printed to a standard output or saved to a configuration file of your choice. Otherwise, you have to ask the realm administrator to issue a new Registration Access Token for your client and send it to you. You can then pass it to any CRUD command via the `--token` option. You can also use the `kcreg config registration-token` command to save the new token in a configuration file and have the Client Registration CLI automatically handle it for you from that point on.

Run the `kcreg update-token --help` command for more information about the `kcreg update-token` command.

### 6.5. 故障排除 {#Troubleshooting}
- Q: When logging in, I get an error: *Parameter client_assertion_type is missing [invalid_client].

  A: This error means your client is configured with `Signed JWT` token credentials, which means you have to use the `--keystore` parameter when logging in.

## 7. 令牌交换 {#Token_Exchange}

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

### 7.1. 内部令牌到内部令牌交换 {#Internal_Token_to_Internal_Token_Exchange}
With an internal token to token exchange you have an existing token minted to a specific client and you want to exchange this token for a new one minted for a different target client. Why would you want to do this? This generally happens when a client has a token minted for itself, and needs to make additional requests to other applications that require different claims and permissions within the access token. Other reasons this type of exchange might be required is if you need to perform a "permission downgrade" where your app needs to invoke on a less trusted app and you don’t want to propagate your current access token.

#### 7.1.1. Granting Permission for the Exchange {#Granting_Permission_for_the_Exchange}
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

#### 7.1.2. Making the Request {#Making_the_Request}
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

### 7.2. 外部令牌交换的内部令牌 {#Internal_Token_to_External_Token_Exchange}
You can exchange a realm token for an externl token minted by an external identity provider. This external identity provider must be configured within the `Identity Provider` section of the admin console. Currently only OAuth/OpenID Connect based external identity providers are supported, this includes all social providers. Keycloak does not perform a backchannel exchange to the external provider. So if the account is not linked, you will not be able to get the external token. To be able to obtain an external token one of these conditions must be met:

- The user must have logged in with the external identity provider at least once
- The user must have linked with the external identity provider through the User Account Service
- The user account was linked through the external identity provider using [Client Initiated Account Linking](https://www.keycloak.org/docs/6.0/server_development/) API.

Finally, the external identity provider must have been configured to store tokens, or, one of the above actions must have been performed with the same user session as the internal token you are exchanging.

If the account is not linked, the exchange response will contain a link you can use to establish it. This is discussed more in the [Making the Request](https://www.keycloak.org/docs/latest/securing_apps/index.html#_internal_external_making_request) section.

#### 7.2.1. Granting Permission for the Exchange {#Granting_Permission_for_the_Exchange}
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

#### 7.2.2. Making the Request {#Making_the_Request}
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

### 7.3. 外部令牌到内部令牌交换 {#External_Token_to_Internal_Token_Exchange}
You can trust and exchange external tokens minted by external identity providers for internal tokens. This can be used to bridge between realms or just to trust tokens from your social provider. It works similarly to an identity provider browser login in that a new user is imported into your realm if it doesn’t exist.

|      | The current limitation on external token exchanges is that if the external token maps to an existing user an exchange will not be allowed unless the existing user already has an account link to the external identity provider. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

When the exchange is complete, a user session will be created within the realm, and you will receive an access and or refresh token depending on the `requested_token_type` parameter value. You should note that this new user session will remain active until it times out or until you call the logout endpoint of the realm passing this new access token.

These types of changes required a configured identity provider in the admin console.

|      | SAML identity providers are not supported at this time. Twitter tokens cannot be exchanged either. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

#### 7.3.1. Granting Permission for the Exchange {#Granting_Permission_for_the_Exchange}
Before external token exchanges can be done, you must grant permission for the calling client to make the exchange. This permission is granted in the same manner as [internal to external permission is granted](https://www.keycloak.org/docs/latest/securing_apps/index.html#_grant_permission_external_exchange).

If you also provide an `audience` parameter whose value points to a different client other than the calling one, you must also grant the calling client permission to exchange to the target client specific in the `audience` parameter. How to do this is [discussed earlier](https://www.keycloak.org/docs/latest/securing_apps/index.html#_client_to_client_permission) in this section.

#### 7.3.2. Making the Request {#Making_the_Request}
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

### 7.4. 模拟 {#Impersonation}
For internal and external token exchanges, the client can request on behalf of a user to impersonate a different user. For example, you may have an admin application that needs to impersonate a user so that a support engineer can debug a problem.

#### 7.4.1. Granting Permission for the Exchange {#Granting_Permission_for_the_Exchange}
The user that the subject token represents must have permission to impersonate other users. See the [Server Administration Guide](https://www.keycloak.org/docs/6.0/server_admin/) on how to enable this permission. It can be done through a role or through fine grain admin permissions.

#### 7.4.2. Making the Request {#Making_the_Request}
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

### 7.5. 直接赤裸裸的模拟 {#Direct_Naked_Impersonation}
You can make an internal token exchange request without providing a `subject_token`. This is called a direct naked impersonation because it places a lot of trust in a client as that client can impersonate any user in the realm. You might need this to bridge for applications where it is impossible to obtain a subject token to exchange. For example, you may be integrating a legacy application that performs login directly with LDAP. In that case, the legacy app is able to authenticate users itself, but not able to obtain a token.

|      | It is very risky to enable direct naked impersonation for a client. If the client’s credentials are ever stolen, that client can impersonate any user in the system. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

#### 7.5.1. Granting Permission for the Exchange {#Granting_Permission_for_the_Exchange}
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

#### 7.5.2. Making the Request {#Making_the_Request}
To make the request, simply specify the `requested_subject` parameter. This must be the username or user id of a valid user. You can also specify an `audience` parameter if you wish.

```
curl -X POST \
    -d "client_id=starting-client" \
    -d "client_secret=geheim" \
    --data-urlencode "grant_type=urn:ietf:params:oauth:grant-type:token-exchange" \
    -d "requested_subject=wburke" \
    http://localhost:8080/auth/realms/myrealm/protocol/openid-connect/token
```

### 7.6. 使用服务帐户展开权限模型 {#Expand_Permission_Model_With_Service_Accounts}
When granting clients permission to exchange, you don’t necessarily have to manually enable those permissions for each and every client. If the client has a service account associated with it, you can use a role to group permissions together and assign exchange permissions by assigning a role to the client’s service account. For example, you might define a `naked-exchange` role and any service account that has that role can do a naked exchange.

### 7.7. 交换漏洞 {#Exchange_Vulnerabilities}
When you start allowing token exchanges, there’s various things you have to both be aware of and careful of.

The first is public clients. Public clients do not have or require a client credential in order to perform an exchange. Anybody that has a valid token will be able to *impersonate* the public client and perform the exchanges that public client is allowed to perform. If there are any untrustworthy clients that are managed by your realm, public clients may open up vulnerabilities in your permission models. This is why direct naked exchanges do not allow public clients and will abort with an error if the calling client is public.

It is possible to exchange social tokens provided by Facebook, Google, etc. for a realm token. Be careful and vigilante on what the exchange token is allowed to do as its not hard to create fake accounts on these social websites. Use default roles, groups, and identity provider mappers to control what attributes and roles are assigned to the external social user.

Direct naked exchanges are quite dangerous. You are putting a lot of trust in the calling client that it will never leak out its client credentials. If those credentials are leaked, then the thief can impersonate anybody in your system. This is in direct contrast to confidential clients that have existing tokens. You have two factors of authentication, the access token and the client credentials, and you’re only dealing with one user. So use direct naked exchanges sparingly.


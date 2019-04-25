# Server Installation and Configuration Guide

[原文地址: https://www.keycloak.org/docs/latest/server_installation/index.html](https://www.keycloak.org/docs/latest/server_installation/index.html)

## 1. 指南概述

本指南的目的是介绍在首次启动Keycloak服务器之前需要完成的步骤。如果您只是想测试Keycloak，它几乎是用它自己的嵌入式和本地数据库开箱即用。对于将要在生产环境中运行的实际部署，您需要决定如何在运行时管理服务器配置(独立或域模式)，为Keycloak存储配置共享数据库，设置加密和HTTPS，最后设置Keycloak在集群中运行。本指南将详细介绍部署服务器之前必须进行的任何预引导决策和设置的各个方面。

需要特别注意的一点是，Keycloak派生自WildFly应用程序服务器。配置Keycloak的许多方面都围绕着WildFly配置元素。如果您想深入了解更多细节，本指南通常会将您引向手册之外的文档。

### 1.1. 建议额外的外部文档

Keycloak构建在WildFly应用服务器之上，它的子项目包括Infinispan(用于缓存)和Hibernate(用于持久性)。本指南只涵盖基础设施级配置的基础知识。强烈建议您仔细阅读WildFly及其子项目的文档。以下是文档链接:

- [*WildFly 16 Documentation*](http://docs.wildfly.org/16/Admin_Guide.html)

## 2. 安装


  安装Keycloak非常简单，只需下载并解压缩即可。本章回顾了系统的需求以及发行版的目录结构。

### 2.1. 系统需求

以下是运行Keycloak身份验证服务器的要求:

- 能运行Java的任何操作系统
- Java 8 JDK
- zip 或者 gzip 和 tar
- 至少512M内存
- At least 1G of diskspace
- 共享的外部数据库，如PostgreSQL、MySQL、Oracle等。如果要在集群中运行，Keycloak需要一个外部共享数据库。有关更多信息，请参阅本指南的[数据库配置](https://www.keycloak.org/docs/latest/server_installation/index.html#_database)部分。
- 如果您想在集群中运行，最好计算机上的网络支持多播。Keycloak可以在没有多播的情况下集群化，但这需要大量的配置更改。有关更多信息，请参见本指南的[集群](https://www.keycloak.org/docs/latest/server_installation/index.html#_clustering)部分。
- 在Linux上，建议使用`/dev/urandom`作为随机数据的来源，以防止由于缺少可用的熵而导致密钥隐藏挂起，除非您的安全策略强制使用`/dev/random`。要在Oracle JDK 8和OpenJDK 8上实现这一点，请设置 `java.security.egd`系统属性为` file:/dev/urandom`。

### 2.2. 安装分布式文件

Keycloak服务器有三个可下载的发行版:

- 'keycloak-6.0.0.[zip|tar.gz]'
- 'keycloak-overlay-6.0.0.[zip|tar.gz]'
- 'keycloak-demo-6.0.0.[zip|tar.gz]'

'keycloak-6.0.0.[zip|tar.gz]'文件是服务器唯一的发行版。它只包含运行Keycloak服务器的脚本和二进制文件。要解压缩这个文件，只需运行操作系统的`unzip`或`gunzip`和`tar`实用程序。

'keycloak-overlay-6.0.0.[zip|tar.gz]'文件是一个WildFly插件，允许您在现有的WildFly发行版上安装Keycloak服务器。我们不支持用户希望在同一服务器实例上运行应用程序和Keycloak。要安装Keycloak服务包，只需将其解压到WildFly发行版的根目录中，打开shell中的bin目录并运行`./jboss-cli.[sh|bat] --file=keycloak-install.cli`。

'keycloak-demo-6.0.0.[zip|tar.gz]' 包含服务器二进制文件、所有文档和所有示例。它预先配置了OIDC和SAML客户机应用程序适配器，可以在不进行任何配置的情况下开箱即用地部署任何分发示例。此分发版只建议那些想要测试Keycloak的用户使用。我们不支持用户在生产环境中运行演示发行版。

要解压缩这些文件，请运行`unzip`或`gunzip`和`tar`实用程序。

### 2.3. 分布式目录结构

本章将介绍服务器分发版的目录结构。

分布式目录结构

![distribution](assets/files.png)

让我们来看看其中一些目录的用途:

- *bin/*

  它包含各种脚本，可以启动服务器，也可以在服务器上执行其他管理操作。

- *domain/*

  当在[域模式](https://www.keycloak.org/docs/latest/server_installation/index.html#_domain-mode)中运行Keycloak时，它包含配置文件和工作目录。

- *modules/*

  这些都是服务器使用的所有Java库。

- *providers/*

  如果您正在为keycloak编写扩展，可以将扩展放在这里。有关这方面的更多信息，请参见[服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/)。

- *standalone/*

  这包含配置文件和工作目录时，运行Keycloak在[独立模式](https://www.keycloak.org/docs/latest/server_installation/index.html#_standalone-mode)。

- *themes/*

  此目录包含用于服务器显示的任何UI所需要的所有html、样式表、JavaScript文件和图像。在这里，您可以修改现有的主题或创建自己的主题。有关这方面的更多信息，请参见[服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/)。

## 3. 选择工作模式

在生产环境中部署Keycloak之前，您需要决定使用哪种类型的操作模式。您会在集群中运行Keycloak吗?您需要一种集中的方式来管理服务器配置吗?您选择的操作模式将影响您如何配置数据库、配置缓存，甚至如何启动服务器。

> Keycloak构建在WildFly应用服务器之上。本指南只讨论在特定模式下部署的基础知识。如果您想了解这方面的具体信息，最好的去处是[*WildFly 16 Documentation*](http://docs.wildfly.org/16/Admin_Guide.html).  

### 3.1. 独立模式

独立的操作模式只在您希望运行一个且仅运行一个Keycloak服务器实例时才有用。它不适用于集群部署，而且所有缓存都是非分布式的，并且只在本地使用。 不建议在生产中使用独立模式，因为只有一个故障点。如果您的单机模式服务器宕机，用户将无法登录。这种模式只适用于测试驱动和使用Keycloak的功能.

#### 3.1.1. 独立的启动脚本

当以独立模式运行服务器时，您需要运行一个特定的脚本来启动服务器，这取决于您的操作系统。这些脚本位于服务器分发版的 *bin/* 目录中。

独立的启动脚本

![standalone boot files](assets/standalone-boot-files.png)

启动服务器:

Linux/Unix

```
$ .../bin/standalone.sh
```

Windows

```
> ...\bin\standalone.bat
```

#### 3.1.2. 独立的配置

本指南的大部分内容将指导您如何配置Keycloak的基础设施级别方面。 这些方面是在配置文件中配置的，该配置文件特定于Keycloak派生的应用程序服务器。在独立操作模式下，该文件位于 *.. / independent /configuration/standalone.xml* 中。 此文件还用于配置特定于Keycloak组件的非基础设施级别的内容。

独立的配置文件

![standalone config file](assets/standalone-config-file.png)

> 在服务器运行时对该文件所做的任何更改都不会生效，甚至可能被服务器覆盖。而是使用命令行脚本或WildFly的web控制台。 更多信息参见[*WildFly 16 Documentation*](http://docs.wildfly.org/16/Admin_Guide.html)。                     

### 3.2. 独立的集群模式

当您希望在集群中运行Keycloak时，可以使用独立集群操作模式。此模式要求在希望运行服务器实例的每台计算机上都有Keycloak分发版的副本。这种模式最初很容易部署，但是会变得相当麻烦。要进行配置更改，您必须修改每台机器上的每个发行版。对于大型集群，这可能会耗费时间并容易出错。

#### 3.2.1. 独立的集群配置

该发行版有一个主要预配置的app服务器配置文件，用于在集群中运行。它具有用于网络、数据库、缓存和发现的所有特定基础设施设置。此文件驻留在 *…/standalone/configuration/standalone-ha.xml*. 这个配置中缺少一些东西。如果不配置共享数据库连接，就不能在集群中运行Keycloak。您还需要在集群前面部署某种类型的负载均衡器。 本指南的[集群](https://www.keycloak.org/docs/latest/server_installation/index.html#_clustering)  和 [数据库](https://www.keycloak.org/docs/latest/server_installation/index.html#_database) 部分将指导您了解这些内容。

标准HA配置

![standalone ha config file](assets/standalone-ha-config-file.png)

> 在服务器运行时对该文件所做的任何更改都不会生效，甚至可能被服务器覆盖。建议使用命令行脚本或WildFly的web控制台。 更多信息参见 [*WildFly 16 Documentation*](http://docs.wildfly.org/16/Admin_Guide.html)                                  |

#### 3.2.2. 独立集群启动脚本

使用与在独立模式下相同的引导脚本启动Keycloak。不同之处在于，您传递了一个额外的标志来指向HA配置文件。

独立集群启动脚本

![standalone boot files](https://www.keycloak.org/docs/latest/server_installation/keycloak-images/standalone-boot-files.png)

启动服务器:

Linux/Unix

```
$ .../bin/standalone.sh --server-config=standalone-ha.xml
```

Windows

```
> ...\bin\standalone.bat --server-config=standalone-ha.xml
```

### 3.3. 域集群模式

域模式是一种集中管理和发布服务器配置的方法。

在标准模式下运行集群会随着集群规模的增长而迅速恶化。每次需要更改配置时，都必须在集群中的每个节点上执行。 域模式解决了这个问题通过提供一个中心位置存储和发布配置。 设置起来可能相当复杂，但最终是值得的。这个功能被内置到WildFly应用服务器中，Keycloak就是从这个应用服务器派生的。

> 本指南将介绍域模式的基本知识。关于如何在集群中设置域模式的详细步骤应该从[*WildFly 16 Documentation*](http://docs.wildfly.org/16/Admin_Guide.html). 获得。

以下是在域模式下运行的一些基本概念。

- 域控制器

  域控制器是一个进程，负责存储、管理和发布集群中每个节点的一般配置。这个进程是集群中的节点获取其配置的中心点。

- 主机控制器

  主机控制器负责管理特定机器上的服务器实例。 您将其配置为运行一个或多个服务器实例。域控制器还可以与每台机器上的主机控制器交互来管理集群。 为了减少运行进程的数量，域控制器还充当它所运行机器上的主机控制器。

- 域配置文件

  域配置文件是一组命名的配置文件，可供服务器用于引导。 域控制器可以定义不同服务器使用的多个域配置文件。

- 服务器组

  服务器组是服务器的集合。它们被管理并配置为一个。您可以将一个域配置文件分配给一个服务器组，该组中的每个服务都将使用该域配置文件作为它们的配置。

在域模式下，在主节点上启动域控制器。集群的配置位于域控制器中。 接下来，在群集中的每台计算机上启动主机控制器。 每个主机控制器部署配置指定将在该计算机上启动的Keycloak服务器实例数。 当主机控制器启动时，它启动的Keycloak服务器实例与配置时一样多。这些服务器实例从域控制器中提取配置。

#### 3.3.1. 域配置

本指南的其他各章将介绍如何配置数据库、HTTP网络连接、缓存和其他与基础设施相关的内容。 虽然独立模式使用 *standalone.xml* 文件来配置这些内容，但域模式使用 *.../domain/configuration/domain.xml* 配置文件。 这里定义了Keycloak 服务器的域配置文件和服务器组。

domain.xml

![domain file](assets/domain-file.png)

> 在域控制器运行时对该文件所做的任何更改都不会生效，甚至可能被服务器覆盖。建议使用命令行脚本或WildFly的web控制台。 更多信息参见[*WildFly 16 Documentation*](http://docs.wildfly.org/16/Admin_Guide.html)

我们来看看这个 *domain.xml* 文件的某些内容。 `auth-server-standalone` 和 `auth-server-clustered` `profile` XML块是您进行大量配置决策的地方。 您将在此处配置网络连接，缓存和数据库连接等内容。

auth-server配置

```
    <profiles>
        <profile name="auth-server-standalone">
            ...
        </profile>
        <profile name="auth-server-clustered">
            ...
        </profile>
```

`auth-server-standalone`配置是非集群设置。 `auth-server-clustered`配置是集群设置。

如果你进一步向下滚动，你会看到定义了各种`socket-binding-groups`。

socket-binding-groups

```
    <socket-binding-groups>
        <socket-binding-group name="standard-sockets" default-interface="public">
           ...
        </socket-binding-group>
        <socket-binding-group name="ha-sockets" default-interface="public">
           ...
        </socket-binding-group>
        <!-- load-balancer-sockets should be removed in production systems and replaced with a better software or hardware based one -->
        <socket-binding-group name="load-balancer-sockets" default-interface="public">
           ...
        </socket-binding-group>
    </socket-binding-groups>
```

此配置定义使用每个Keycloak服务器实例打开的各种连接器的默认端口映射。 包含`$ {...}`的任何值都是可以在命令行上使用`-D`开关重写的值，例如:

```
$ domain.sh -Djboss.http.port=80
```

Keycloak服务器组的定义位于 `server-groups` XML块中。 它指定在主机控制器启动实例时使用的域配置文件(`default`)以及Java VM的一些默认引导参数。 它还将`socket-binding-group`绑定到服务器组。

server group

```
    <server-groups>
        <!-- load-balancer-group should be removed in production systems and replaced with a better software or hardware based one -->
        <server-group name="load-balancer-group" profile="load-balancer">
            <jvm name="default">
                <heap size="64m" max-size="512m"/>
            </jvm>
            <socket-binding-group ref="load-balancer-sockets"/>
        </server-group>
        <server-group name="auth-server-group" profile="auth-server-clustered">
            <jvm name="default">
                <heap size="64m" max-size="512m"/>
            </jvm>
            <socket-binding-group ref="ha-sockets"/>
        </server-group>
    </server-groups>
```

#### 3.3.2. 主机控制器配置

Keycloak附带了两个主机控制器配置文件，它们位于 *.../domain/configuration/* 目录中：*host-master.xml* 和 *host-slave.xml*。 *host-master.xml* 配置为启动域控制器，负载均衡器和一个Keycloak服务器实例。 *host-slave.xml* 配置为与域控制器通信并启动一个Keycloak服务器实例。

> 负载均衡器不是必需的服务。 它的存在使您可以轻松地在开发计算机上测试驱动器群集。 虽然可以在生产中使用，但如果您要使用其他基于硬件或软件的负载均衡器，则可以选择替换它。

主机控制器配置

![host files](assets/host-files.png)

要禁用负载均衡器服务器实例，请编辑 *host-master.xml* 并注释掉或删除 `"load-balancer"` 条目。

```
    <servers>
        <!-- remove or comment out next line -->
        <server name="load-balancer" group="loadbalancer-group"/>
        ...
    </servers>
```

关于此文件的另一个有趣的事情是声明身份验证服务器实例。 它有一个`port-offset`设置。 *domain.xml*`socket-binding-group`或服务器组中定义的任何网络端口都将添加`port-offset`的值。 对于此示例域设置，我们执行此操作，以便负载平衡器服务器打开的端口不会与启动的身份验证服务器实例冲突。

```
    <servers>
        ...
        <server name="server-one" group="auth-server-group" auto-start="true">
             <socket-bindings port-offset="150"/>
        </server>
    </servers>
```

#### 3.3.3. 服务器实例工作目录

主机文件中定义的每个Keycloak服务器实例在 *…/domain/servers/{SERVER NAME}* 下创建一个工作目录。可以在其中进行其他配置，并且服务器实例需要或创建的任何临时，日志或数据文件也可以放在那里。 每个服务器目录的结构最终看起来像任何其他WildFly启动的服务器。

工作目录

![domain server dir](assets/domain-server-dir.png)

#### 3.3.4. 域启动脚本

在域模式下运行服务器时，根据您的操作系统，需要运行特定的脚本来启动服务器。 这些脚本位于服务器分发的 *bin/* 目录中。

域启动脚本

![domain boot files](assets/domain-boot-files.png)

启动服务器:

Linux/Unix

```
$ .../bin/domain.sh --host-config=host-master.xml
```

Windows

```
> ...\bin\domain.bat --host-config=host-master.xml
```

运行启动脚本时，您需要通过`--host-config`开关传入您将要使用的主机控制配置文件。

#### 3.3.5. 集群域示例

您可以使用开箱即用的 *domain.xml* 配置测试驱动器集群。这个示例域是用来在一台机器上运行并启动的:

- 1个域控制器
- 1个HTTP负载均衡器
- 2个Keycloak服务器实例

要模拟在两台计算机上运行集群，您将运行`domain.sh`脚本两次以启动两个单独的主机控制器。 第一个是主控主机控制器，它将启动域控制器，HTTP负载平衡器和一个Keycloak认证服务器实例。 第二个是从属主机控制器，它只启动一个认证服务器实例。

##### 设置从属控制器到域控制器的连接

在启动之前，您必须配置从属主机控制器，以便它可以安全地与域控制器通信。如果不这样做，则从属主机将无法从域控制器获取集中式配置。 要设置安全连接，您必须创建服务器管理员用户和将在主服务器和从服务器之间共享的密钥。 您可以通过运行 `…/bin/add-user.sh` 脚本来完成此操作。

当您运行脚本时，选择 `Management User` 并回答 `yes` ，当它询问您是否将新用户用于一个AS进程以连接到另一个AS进程时。 这将生成一个秘密值，您需要将其剪切并粘贴到 *…/domain/configuration/host-slave.xml* 文件中。

添加: App Server Admin

```bash
$ add-user.sh
 What type of user do you wish to add?
  a) Management User (mgmt-users.properties)
  b) Application User (application-users.properties)
 (a): a
 Enter the details of the new user to add.
 Using realm 'ManagementRealm' as discovered from the existing property files.
 Username : admin
 Password recommendations are listed below. To modify these restrictions edit the add-user.properties configuration file.
  - The password should not be one of the following restricted values {root, admin, administrator}
  - The password should contain at least 8 characters, 1 alphabetic character(s), 1 digit(s), 1 non-alphanumeric symbol(s)
  - The password should be different from the username
 Password :
 Re-enter Password :
 What groups do you want this user to belong to? (Please enter a comma separated list, or leave blank for none)[ ]:
 About to add user 'admin' for realm 'ManagementRealm'
 Is this correct yes/no? yes
 Added user 'admin' to file '/.../standalone/configuration/mgmt-users.properties'
 Added user 'admin' to file '/.../domain/configuration/mgmt-users.properties'
 Added user 'admin' with groups to file '/.../standalone/configuration/mgmt-groups.properties'
 Added user 'admin' with groups to file '/.../domain/configuration/mgmt-groups.properties'
 Is this new user going to be used for one AS process to connect to another AS process?
 e.g. for a slave host controller connecting to the master or for a Remoting connection for server to server EJB calls.
 yes/no? yes
 To represent the user add the following to the server-identities definition <secret value="bWdtdDEyMyE=" />
```

> add-user.sh不会将用户添加到Keycloak服务器，而是添加到基础JBoss企业应用程序平台。 上述脚本中使用和生成的凭据仅用于示例目的。 请使用系统上生成的。   

现在将秘密值剪切并粘贴到 *…/domain/configuration/host-slave.xml* 文件中，如下所示：

```xml
     <management>
         <security-realms>
             <security-realm name="ManagementRealm">
                 <server-identities>
                     <secret value="bWdtdDEyMyE="/>
                 </server-identities>
```

您还需要在 *…/domain/configuration/host-slave.xml* 文件中添加已创建用户的 *username*：

```xml
     <remote security-realm="ManagementRealm" username="admin">
```

##### 运行启动脚本

由于我们在一台开发机器上模拟双节点集群，因此您将运行两次启动脚本：

启动 主

```
$ domain.sh --host-config=host-master.xml
```

启动 从属

```
$ domain.sh --host-config=host-slave.xml
```

要试用它，请打开浏览器并转到 <http://localhost:8080/auth>。

### 3.4. 跨数据中心复制模式

跨数据中心复制模式适用于您希望跨多个数据中心在集群中运行Keycloak，最常用的是使用位于不同地理区域的数据中心站点。 使用此模式时，每个数据中心都有自己的Keycloak服务器集群。

本文档将引用以下示例体系结构图来说明和描述简单的跨数据中心复制用例。

示例架构图

![cross dc architecture](assets/cross-dc-architecture.png)

#### 3.4.1. 先决条件

由于这是一个高级主题，我们建议您首先阅读以下内容，它们提供了宝贵的背景知识：

- [集群与Keycloak](https://www.keycloak.org/docs/6.0/server_installation/#_clustering) 设置跨数据中心复制时，您将使用更多独立的Keycloak集群，因此您必须了解集群的工作方式以及基本概念和要求，例如负载平衡，共享数据库和多播。
- [JBoss数据网格跨数据中心复制](https://access.redhat.com/documentation/en-us/red_hat_data_grid/7.3/html/red_hat_data_grid_user_guide/x_site_replication) Keycloak使用JBoss Data Grid（JDG）在数据中心之间复制Infinispan数据。

#### 3.4.2. 技术细节

本节介绍了如何完成Keycloak跨数据中心复制的概念和详细信息。

Data(数据)

Keycloak是有状态的应用程序。 它使用以下作为数据源：

- 数据库用于保存永久数据，例如用户信息。
- Infinispan缓存用于缓存来自数据库的持久性数据，还用于保存一些短期和频繁更改的元数据，例如用于用户会话。 Infinispan通常比数据库快得多，但是使用Infinispan保存的数据不是永久性的，并且预计不会在集群重启期间持续存在。

在我们的示例架构中，有两个名为 `site1` 和 `site2` 的数据中心。 对于跨数据中心复制，我们必须确保两个数据源都可靠地工作，并且来自 `site1` 的Keycloak服务器最终能够读取`site2`上的Keycloak服务器保存的数据。

根据环境，您可以选择是否愿意：

- 可靠性 - 通常用于 `主/主` 模式。 写在`site1`上的数据必须立即在`site2`上可见。
- 性能 - 通常用于 `主/被`模式。 写在`site1`上的数据不需要立即在`site2`上可见。 在某些情况下，数据可能在`site2`上根本不可见。

For more details, see [Modes](https://www.keycloak.org/docs/latest/server_installation/index.html#modes).

#### 3.4.3. 请求处理

最终用户的浏览器向[前端负载均衡器](https://www.keycloak.org/docs/6.0/server_installation/#_setting-up-a-load-balancer-or-proxy)发送HTTP请求。 此负载均衡器通常是HTTPD或WildFly，带有mod_cluster，NGINX，HA代理，或者某些其他类型的软件或硬件负载均衡器。

然后，负载均衡器将其接收的HTTP请求转发到基础Keycloak实例，这些实例可以在多个数据中心之间传播。 负载平衡器通常为[粘性会话](https://www.keycloak.org/docs/6.0/server_installation/#sticky-sessions)提供支持, 这意味着负载均衡器能够始终将来自同一用户的所有HTTP请求转发到同一数据中心内的同一Keycloak实例。

从客户端应用程序发送到负载均衡器的HTTP请求称为“反向通道请求”。终端用户的浏览器不会看到这些，因此不能作为用户和负载平衡器之间的粘性会话的一部分。 对于反向信道请求，负载均衡器可以将HTTP请求转发到任何数据中心中的任何Keycloak实例。 这很有挑战性，因为一些OpenID Connect和一些SAML流需要来自用户和应用程序的多个HTTP请求。 由于我们不能可靠地依赖粘性会话来强制将所有相关请求发送到同一数据中心中的同一个Keycloak实例，因此我们必须跨数据中心复制一些数据，以便在特定流期间由后续HTTP请求查看数据。

#### 3.4.4. Modes (模式)

根据您的要求，跨数据中心复制有两种基本操作模式：

- 主/备 - 这里，用户和客户端应用程序仅将请求发送到单个数据中心的Keycloak节点。 第二个数据中心仅用作保存数据的`备份`。 如果主数据中心出现故障，通常可以从第二个数据中心恢复数据。
- 主/主 - 这里，用户和客户端应用程序将请求发送到两个数据中心的Keycloak节点。 这意味着数据需要立即在两个站点上可见，并且可以立即从两个站点上的Keycloak服务器中使用。 如果Keycloak服务器在`site1`上写入一些数据，并且要求在`site1`上的写入完成后，立即可以通过`site2`上的Keycloak服务器读取数据。

`主动/被动`模式对性能更好。 有关如何为任一模式配置高速缓存的详细信息，请参阅：[SYNC或ASYNC备份](https://www.keycloak.org/docs/latest/server_installation/index.html#backups).

#### 3.4.5. 数据库

Keycloak使用关系数据库管理系统(RDBMS)来持久保存有关领域，客户端，用户等的一些元数据。 有关详细信息，请参阅服务器安装指南的[本章](https://www.keycloak.org/docs/6.0/server_installation/#_database)。 在跨数据中心复制设置中，我们假设两个数据中心都与同一个数据库通信，或者每个数据中心都有自己的数据库节点，并且两个数据库节点在数据中心之间同步复制。 在这两种情况下，当`site1`上的Keycloak服务器持久保存某些数据并提交事务时，要求这些数据立即在`site2`上的后续数据库事务中可见。

数据库设置的细节超出了Keycloak的范围，但是像MariaDB和Oracle这样的许多RDBMS供应商都提供了复制数据库和同步复制。 我们与这些供应商一起测试Keycloak：

- Oracle Database 12c Release 1 (12.1) RAC
- Galera 3.12 cluster for MariaDB server version 10.1.19-MariaDB

#### 3.4.6. Infinispan缓存

本节首先介绍Infinispan缓存的高级描述。 下面是缓存设置的更多细节。

Authentication sessions (认证会话)

在Keycloak中，我们有认证会话的概念。 有一个名为 `authenticationSessions` 的独立Infinispan缓存用于在特定用户的身份验证期间保存数据。 来自此缓存的请求通常只涉及浏览器和Keycloak服务器，而不是应用程序。 在这里，我们可以依赖粘性会话，即使您处于`主/主` 模式，也不需要跨数据中心复制 `authenticationSessions` 缓存内容。

Action tokens (动作令牌)

我们还有[动作令牌](https://www.keycloak.org/docs/6.0/server_development/#_action_token_spi)的概念， 通常用于用户需要通过电子邮件异步地确认操作的场景。例如，在“忘记密码”流期间，`actiontoken`Infinispan缓存用于跟踪有关操作令牌的元数据，比如已经使用了哪个操作令牌，因此不能第二次重用。这通常需要跨数据中心复制。

持久数据的缓存和失效

Keycloak使用Infinispan来缓存持久数据，以避免对数据库的许多不必要的请求。 缓存提高了性能，但它增加了额外的挑战。 当某些Keycloak服务器更新任何数据时，所有数据中心中的所有其他Keycloak服务器都需要知道它，因此它们会使其缓存中的特定数据无效。 Keycloak使用称为`realms` ，`users` 和 `authorization` 的本地Infinispan缓存来缓存持久数据。

我们使用单独的缓存 `work`，它在所有数据中心中复制。 工作缓存本身不会缓存任何实际数据。 它仅用于在群集节点和数据中心之间发送失效消息。 换句话说，当更新数据时，例如用户`john`，Keycloak节点将失效消息发送到同一数据中心的所有其他集群节点以及所有其他数据中心。 收到无效通知后，每个节点都会从其本地缓存中使相应的数据无效。

User sessions (用户会话)

Infinispan缓存称为 `sessions`, `clientSessions`, `offlineSessions` 和 `offlineClientSessions`，所有这些缓存通常都需要跨数据中心进行复制。 这些缓存用于保存有关用户会话的数据，这些数据对用户的浏览器会话长度有效。 缓存必须处理来自最终用户和应用程序的HTTP请求。 如上所述，在此实例中无法可靠地使用粘性会话，但我们仍希望确保后续HTTP请求可以查看最新数据。 因此，数据通常在数据中心之间复制。

Brute force protection (强力保护)

最后，`loginFailures` 缓存用于跟踪有关失败登录的数据，例如用户`john` 输入错误密码的次数。 详细说明[此处](https://www.keycloak.org/docs/6.0/server_admin/#password-guess-brute-force-attacks)。 管理员是否应该跨数据中心复制此缓存。 要准确计算登录失败次数，需要进行复制。 另一方面，不复制此数据可以节省一些性能。 因此，如果性能比准确的登录失败计数更重要，则可以避免复制。

有关如何配置高速缓存的更多详细信息，请参阅[调整JDG高速缓存配置](https://www.keycloak.org/docs/latest/server_installation/index.html#tuningcache)。

#### 3.4.7. 通信细节

keycover使用多个独立的Infinispan缓存集群。每个Keycloak 节点都与相同数据中心中的其他Keycloak 节点在集群中，但不包含不同数据中心的Keycloak节点。 Keycloak节点不直接与来自不同数据中心的Keycloak节点通信。 Keycloak节点使用外部JDG（实际上是Infinispan服务器）跨数据中心进行通信。 这是使用[Infinispan HotRod协议](http://infinispan.org/docs/8.2.x/user_guide/user_guide.html#using_hot_rod_server)。

Keycloak端上的Infinispan缓存必须配置为[remoteStore](http://infinispan.org/docs/8.2.x/user_guide/user_guide.html#remote_store)，以确保数据被保存到远程缓存中。JDG服务器之间有单独的Infinispan集群，因此保存在 `site1` 上的JDG1上的数据被复制到 `site2`上的JDG2上。

最后，接收JDG服务器通过客户机侦听器通知集群中的Keycloak服务器，这是HotRod协议的一个特性。然后更新它们的Infinispan缓存，特定的用户会话也可以在 `site2` 的Keycloak节点上看到。

有关更多细节，请参见[示例架构图](https://www.keycloak.org/docs/latest/server_installation/index.html#archdiagram)。

#### 3.4.8. 基本设置

在本例中，我们描述了使用两个数据中心，`site1` 和 `site2`。 每个数据中心由1个Infinispan服务器和2个Keycloak服务器组成。 我们最终将拥有2台Infinispan服务器和4台Keycloak服务器。

- `Site1` 由Infinispan服务器，`jdg1` 和2个Keycloak服务器，`node11` 和 `node12` 组成。
- `Site2` 由Infinispan服务器，`jdg2` 和2个Keycloak服务器，`node21` 和 `node22` 组成。
- Infinispan服务器 `jdg1` 和 `jdg2` 通过 RELAY2 协议和 `backup` 的Infinispan缓存相互连接，其方式与[JDG文档](https://access.redhat.com/documentation/en-us/red_hat_data_grid/7.3/html/red_hat_data_grid_user_guide/x_site_replication)中描述的类似。
- Keycloak服务器 `node11` 和 `node12` 彼此形成一个集群，但它们不直接与 `site2` 中的任何服务器通信。 它们使用HotRod协议（远程缓存）与Infinispan服务器 `jdg1` 进行通信。 有关详细信息，请参阅[通信详细信息](https://www.keycloak.org/docs/latest/server_installation/index.html#communication)。
- 同样的细节适用于 `node21` 和 `node22`。它们彼此集群，仅使用HotRod协议与`jdg2`服务器通信。

我们的示例设置假定所有4个Keycloak服务器都与同一个数据库通信。 在生产中，建议在数据库中使用单独的同步复制数据库，如[数据库](https://www.keycloak.org/docs/latest/server_installation/index.html#database)中所述。

##### Infinispan服务器设置

请按照以下步骤设置Infinispan服务器：

1. 下载Infinispan 9.4.8服务器并解压缩到您选择的目录。 该位置将在后面的步骤中称为 `JDG1_HOME` 。

2. 在JGroups子系统的配置中更改 `JDG1_HOME/standalone/configuration/clustered.xml`中的那些内容：

   1. 在 `channels` 元素下添加 `xsite` 通道，它将使用`tcp` 堆栈：

      ```xml
      <channels default="cluster">
          <channel name="cluster"/>
          <channel name="xsite" stack="tcp"/>
      </channels>
      ```

   2. 在 `udp` 堆栈的末尾添加 `relay` 元素。 我们将以我们的站点为 `site1` 的方式配置它，而我们将备份的另一个站点是 `site2` ：

      ```xml
      <stack name="udp">
          ...
          <relay site="site1">
              <remote-site name="site2" channel="xsite"/>
              <property name="relay_multicasts">false</property>
          </relay>
      </stack>
      ```

   3. 配置`tcp`堆栈使用`TCPPING`协议而不是'MPING`。 删除`MPING`元素并将其替换为`TCPPING`。 `initial_hosts`元素指向主机`jdg1`和`jdg2`：

      ```xml
      <stack name="tcp">
          <transport type="TCP" socket-binding="jgroups-tcp"/>
          <protocol type="TCPPING">
              <property name="initial_hosts">jdg1[7600],jdg2[7600]</property>
              <property name="ergonomics">false</property>
          </protocol>
          <protocol type="MERGE3"/>
          ...
      </stack>
      ```

      > 这只是一个让程序快速运行的示例设置。在生产中，JGroups ' RELAY2 '不需要使用' tcp '栈， 但是您可以配置任何其他堆栈。例如，如果数据中心之间的网络支持多播，可以使用默认的udp堆栈。 只要确保Infinispan和Keycloak集群是相互不可发现的。 同样，您不需要使用 `TCPPING` 作为发现协议。 在生产中，你可能不会使用`TCPPING`因为它是静态的。 最后，站点名称也是可配置的。 这个更详细的设置的详细信息超出了Keycloak文档的范围。 有关更多详细信息，请参阅Infinispan文档和JGroups文档。

3. 将其添加到名为`clustered`的缓存容器下的`JDG1_HOME/standalone/configuration/clustered.xml`中：

   ```xml
   <cache-container name="clustered" default-cache="default" statistics="true">
           ...
           <replicated-cache-configuration name="sessions-cfg" mode="SYNC" start="EAGER" batching="false">
               <locking acquire-timeout="0" />
               <backups>
                   <backup site="site2" failure-policy="FAIL" strategy="SYNC" enabled="true">
                       <take-offline min-wait="60000" after-failures="3" />
                   </backup>
               </backups>
           </replicated-cache-configuration>
   
           <replicated-cache name="work" configuration="sessions-cfg"/>
           <replicated-cache name="sessions" configuration="sessions-cfg"/>
           <replicated-cache name="clientSessions" configuration="sessions-cfg"/>
           <replicated-cache name="offlineSessions" configuration="sessions-cfg"/>
           <replicated-cache name="offlineClientSessions" configuration="sessions-cfg"/>
           <replicated-cache name="actionTokens" configuration="sessions-cfg"/>
           <replicated-cache name="loginFailures" configuration="sessions-cfg"/>
   
   </cache-container>
   ```

   > 有关 `replicated-cache-configuration` 中的配置选项的详细信息，请参阅[调整JDG缓存配置](https://www.keycloak.org/docs/latest/server_installation/index.html#tuningcache)，其中包含信息 关于调整其中一些选项。

   > 与以前的版本不同，Infinispan服务器 `replicated-cache-configuration` 需要在没有 `transaction` 元素的情况下进行配置。 有关详细信息，请参阅[故障排除](https://www.keycloak.org/docs/latest/server_installation/index.html#troubleshooting) 。

4. 在通过网络访问受保护的缓存之前，某些Infinispan服务器版本需要授权。

   > 如果您使用推荐的Infinispan 9.4.8服务器，则不应该看到任何问题，并且可以（并且应该）忽略此步骤。 与授权相关的问题可能仅适用于Infinispan服务器的某些其他版本。

   Keycloak需要更新包含脚本的 `___ script_cache` 缓存。 如果访问此缓存时出错，则需要在 `clustered.xml` 配置中设置授权，如下所述：

   1. 在 `<management>` 部分中，添加一个安全领域：

      ```xml
      <management>
          <security-realms>
              ...
              <security-realm name="AllowScriptManager">
                  <authentication>
                      <users>
                          <user username="___script_manager">
                              <password>not-so-secret-password</password>
                          </user>
                      </users>
                  </authentication>
              </security-realm>
          </security-realms>
      ```

   2. 在服务器核心子系统中，添加 `<security>` ，如下所示：

      ```xml
      <subsystem xmlns="urn:infinispan:server:core:8.4">
          <cache-container name="clustered" default-cache="default" statistics="true">
              <security>
                  <authorization>
                      <identity-role-mapper/>
                      <role name="___script_manager" permissions="ALL"/>
                  </authorization>
              </security>
              ...
      ```

   3. 在端点子系统中，将身份验证配置添加到Hot Rod连接器：

      ```xml
      <subsystem xmlns="urn:infinispan:server:endpoint:8.1">
          <hotrod-connector cache-container="clustered" socket-binding="hotrod">
              ...
              <authentication security-realm="AllowScriptManager">
                  <sasl mechanisms="DIGEST-MD5" qop="auth" server-name="keycloak-jdg-server">
                      <policy>
                          <no-anonymous value="false" />
                      </policy>
                  </sasl>
              </authentication>
      ```

5. 将服务器复制到第二个位置，稍后将称之为 `JDG2_HOME`。

6. 在 `JDG2_HOME/standalone/configuration/clustered.xml`交换`site1`和`site2`，反之亦然，在JGroups子系统中的 `relay` 配置和cache-subsystem中 `backups` 的配置。 例如：

   1. `relay`元素应如下所示：

      ```xml
      <relay site="site2">
          <remote-site name="site1" channel="xsite"/>
          <property name="relay_multicasts">false</property>
      </relay>
      ```

   2. 像这样的`backups`元素：

      ```xml
                  <backups>
                      <backup site="site1" ....
                      ...
      ```

      由于Infinispan子系统不支持用表达式替换站点名，因此目前需要为两个站点上的JDG服务器提供不同的配置文件。有关详细信息，请参见[this issue](https://issues.jboss.org/browse/WFLY-9458)。

7. 启动服务器 `jdg1`:

   ```bash
   cd JDG1_HOME/bin
   ./standalone.sh -c clustered.xml -Djava.net.preferIPv4Stack=true \
     -Djboss.default.multicast.address=234.56.78.99 \
     -Djboss.node.name=jdg1 -b PUBLIC_IP_ADDRESS
   ```

8. 启动服务器`jdg2`。由于存在不同的组播地址，因此`jdg1`和`jdg2`服务器并不直接集群在一起;相反，它们只是通过RELAY2协议连接，TCP JGroups堆栈用于它们之间的通信。启动命令是这样的:

   ```bash
   cd JDG2_HOME/bin
   ./standalone.sh -c clustered.xml -Djava.net.preferIPv4Stack=true \
     -Djboss.default.multicast.address=234.56.78.100 \
     -Djboss.node.name=jdg2 -b PUBLIC_IP_ADDRESS
   ```

9. 要验证此时通道是否工作，您可能需要使用JConsole并连接到正在运行的`JDG1`或`JDG2`服务器。当您使用MBean `jgroups:type=protocol,cluster="cluster",protocol=RELAY2`和操作`printRoutes`时，应该会看到如下输出:

   ```
   site1 --> _jdg1:site1
   site2 --> _jdg2:site2
   ```

   当您使用MBean  `jgroups:type=protocol,cluster="cluster",protocol=GMS` 时，您应该看到属性成员只包含一个成员:

   1. 在`JDG1`上应该是这样的:

      ```
      (1) jdg1
      ```

   2. 在JDG2上是这样的:

      ```
      (1) jdg2
      ```

      > 在生产中，您可以在每个数据中心拥有更多Infinispan服务器。 您只需要确保同一数据中心内的Infinispan服务器使用相同的多播地址（换句话说，在启动时使用相同的 `jboss.default.multicast.address` ）。 然后在`GMS` 协议视图的jconsole中，您将看到当前集群的所有成员。

##### Keycloak服务器设置

1. 将Keycloak服务器分发解压缩到您选择的位置。 它将在后面称为 `NODE11`。

2. 为KeycloakDS数据源配置共享数据库。 建议使用MySQL或MariaDB进行测试。 有关详细信息，请参阅[数据库](https://www.keycloak.org/docs/latest/server_installation/index.html#database)。

   在生产中，您可能需要在每个数据中心都有一个单独的数据库服务器，并且两个数据库服务器应该同步复制到彼此。 在示例设置中，我们只使用一个数据库并将所有4个Keycloak服务器连接到它。

3. 编辑 `NODE11/standalone/configuration/standalone-ha.xml` :

   1. 将属性`site`添加到JGroups UDP协议：

      ```xml
                        <stack name="udp">
                            <transport type="UDP" socket-binding="jgroups-udp" site="${jboss.site.name}"/>
      ```

   2. 在名为`keycloak`的`cache-container`元素下添加这个`module`属性：

      ```xml
       <cache-container name="keycloak" module="org.keycloak.keycloak-model-infinispan">
      ```

   3. 在`work`缓存下添加`remote-store`：

      ```xml
      <replicated-cache name="work">
          <remote-store cache="work" remote-servers="remote-cache" passivation="false" fetch-state="false" purge="false" preload="false" shared="true">
              <property name="rawValues">true</property>
              <property name="marshaller">org.keycloak.cluster.infinispan.KeycloakHotRodMarshallerFactory</property>
          </remote-store>
      </replicated-cache>
      ```

   4. 在`sessions`缓存下添加这样的`remote-store`：

      ```xml
      <distributed-cache name="sessions" owners="1">
          <remote-store cache="sessions" remote-servers="remote-cache" passivation="false" fetch-state="false" purge="false" preload="false" shared="true">
              <property name="rawValues">true</property>
              <property name="marshaller">org.keycloak.cluster.infinispan.KeycloakHotRodMarshallerFactory</property>
          </remote-store>
      </distributed-cache>
      ```

   5. 对于`offlineSessions`，`clientSessions`，`offlineClientSessions`，`loginFailures`和`actionTokens`缓存执行相同的操作（与`sessions`缓存的唯一区别是`cache`属性值不同）：

      ```xml
      <distributed-cache name="offlineSessions" owners="1">
          <remote-store cache="offlineSessions" remote-servers="remote-cache" passivation="false" fetch-state="false" purge="false" preload="false" shared="true">
              <property name="rawValues">true</property>
              <property name="marshaller">org.keycloak.cluster.infinispan.KeycloakHotRodMarshallerFactory</property>
          </remote-store>
      </distributed-cache>
      
      <distributed-cache name="clientSessions" owners="1">
          <remote-store cache="clientSessions" remote-servers="remote-cache" passivation="false" fetch-state="false" purge="false" preload="false" shared="true">
              <property name="rawValues">true</property>
              <property name="marshaller">org.keycloak.cluster.infinispan.KeycloakHotRodMarshallerFactory</property>
          </remote-store>
      </distributed-cache>
      
      <distributed-cache name="offlineClientSessions" owners="1">
          <remote-store cache="offlineClientSessions" remote-servers="remote-cache" passivation="false" fetch-state="false" purge="false" preload="false" shared="true">
              <property name="rawValues">true</property>
              <property name="marshaller">org.keycloak.cluster.infinispan.KeycloakHotRodMarshallerFactory</property>
          </remote-store>
      </distributed-cache>
      
      <distributed-cache name="loginFailures" owners="1">
          <remote-store cache="loginFailures" remote-servers="remote-cache" passivation="false" fetch-state="false" purge="false" preload="false" shared="true">
              <property name="rawValues">true</property>
              <property name="marshaller">org.keycloak.cluster.infinispan.KeycloakHotRodMarshallerFactory</property>
          </remote-store>
      </distributed-cache>
      
      <distributed-cache name="actionTokens" owners="2">
          <object-memory size="-1"/>
          <expiration max-idle="-1" interval="300000"/>
          <remote-store cache="actionTokens" remote-servers="remote-cache" passivation="false" fetch-state="false" purge="false" preload="true" shared="true">
              <property name="rawValues">true</property>
              <property name="marshaller">org.keycloak.cluster.infinispan.KeycloakHotRodMarshallerFactory</property>
          </remote-store>
      </distributed-cache>
      ```

   6. 将远程存储的出站套接字绑定添加到`socket-binding-group`元素配置中：

      ```xml
      <outbound-socket-binding name="remote-cache">
          <remote-destination host="${remote.cache.host:localhost}" port="${remote.cache.port:11222}"/>
      </outbound-socket-binding>
      ```

   7. 分布式缓存`authenticationSessions`和其他缓存的配置保持不变。

   8. （可选）在`logging`子系统下启用DEBUG日志记录：

      ```xml
      <logger category="org.keycloak.cluster.infinispan">
          <level name="DEBUG"/>
      </logger>
      <logger category="org.keycloak.connections.infinispan">
          <level name="DEBUG"/>
      </logger>
      <logger category="org.keycloak.models.cache.infinispan">
          <level name="DEBUG"/>
      </logger>
      <logger category="org.keycloak.models.sessions.infinispan">
          <level name="DEBUG"/>
      </logger>
      ```

4. 将`NODE11`复制到3个其他目录，后面称为`NODE12`，`NODE21`和`NODE22`。

5. 启动 `NODE11` :

   ```bash
   cd NODE11/bin
   ./standalone.sh -c standalone-ha.xml -Djboss.node.name=node11 -Djboss.site.name=site1 \
     -Djboss.default.multicast.address=234.56.78.1 -Dremote.cache.host=jdg1 \
     -Djava.net.preferIPv4Stack=true -b PUBLIC_IP_ADDRESS
   ```

6. 启动 `NODE12` :

   ```bash
   cd NODE12/bin
   ./standalone.sh -c standalone-ha.xml -Djboss.node.name=node12 -Djboss.site.name=site1 \
     -Djboss.default.multicast.address=234.56.78.1 -Dremote.cache.host=jdg1 \
     -Djava.net.preferIPv4Stack=true -b PUBLIC_IP_ADDRESS
   ```

   应该连接集群节点。类似这样的东西应该同时出现在NODE11和NODE12的日志中:

   ```
   Received new cluster view for channel keycloak: [node11|1] (2) [node11, node12]
   ```

   > 日志中的通道名称可能不同。

7. 启动 `NODE21` :

   ```bash
   cd NODE21/bin
   ./standalone.sh -c standalone-ha.xml -Djboss.node.name=node21 -Djboss.site.name=site2 \
     -Djboss.default.multicast.address=234.56.78.2 -Dremote.cache.host=jdg2 \
     -Djava.net.preferIPv4Stack=true -b PUBLIC_IP_ADDRESS
   ```

   它不应该使用`NODE11`和`NODE12`连接到集群，而是分开一个：

   ```
   Received new cluster view for channel keycloak: [node21|0] (1) [node21]
   ```

8. 启动 `NODE22` :

   ```bash
   cd NODE22/bin
   ./standalone.sh -c standalone-ha.xml -Djboss.node.name=node22 -Djboss.site.name=site2 \
     -Djboss.default.multicast.address=234.56.78.2 -Dremote.cache.host=jdg2 \
     -Djava.net.preferIPv4Stack=true -b PUBLIC_IP_ADDRESS
   ```

   它应该在与`NODE21`的集群中：

   ```
   Received new cluster view for channel keycloak: [node21|1] (2) [node21, node22]
   ```

   > 日志中的通道名称可能不同。 

9. 测试:

   1. 转到 `http://node11:8080/auth/` 并创建初始管理员用户。

   2. 转到 `http://node11:8080/auth/admin` 并以admin身份登录管理控制台。

   3. 打开第二个浏览器并转到任何节点 `http://node12:8080/auth/admin` 或`http://node21:8080/auth/admin` 或  `http://node22:8080/auth/admin` 。 登录后，您应该能够在所有4台服务器上的特定用户，客户端或领域的“会话”选项卡中看到相同的会话。

   4. 在对Keycloak管理控制台进行任何更改后（例如，更新某些用户或某些领域），更新应立即在4个节点中的任何一个上可见，因为缓存应在任何地方正确无效。

   5. 如果需要，请检查server.logs。 登录或注销后，这样的消息应该在所有节点上 `NODEXY/standalone/log/server.log`：

      ```
      2017-08-25 17:35:17,737 DEBUG [org.keycloak.models.sessions.infinispan.remotestore.RemoteCacheSessionListener] (Client-Listener-sessions-30012a77422542f5) Received event from remote store.
      Event 'CLIENT_CACHE_ENTRY_REMOVED', key '193489e7-e2bc-4069-afe8-f1dfa73084ea', skip 'false'
      ```

#### 3.4.9. 跨DC部署的管理

本节包含与跨数据中心复制相关的一些提示和选项。

- 当您在数据中心内运行Keycloak服务器时， 需要在 `KeycloakDS` 数据源中引用的数据库已经在该数据中心中运行并可用。 `outbound-socket-binding`引用的Infinispan服务器也是必要的， 从Infinispan缓存`remote-store`元素引用的,已经在运行。否则Keycloak服务器将无法启动。
- 如果要支持数据库故障转移和更高的可靠性，每个数据中心都可以拥有更多数据库节点。 有关如何在数据库端进行设置以及如何在Keycloak端配置`KeycloakDS`数据源的详细信息，请参阅数据库和JDBC驱动程序的文档。
- 每个数据中心都可以在集群中运行更多Infinispan服务器。 如果您需要一些故障转移和更好的容错能力，这将非常有用。 用于Infinispan服务器和Keycloak服务器之间通信的HotRod协议具有以下功能：Infinispan服务器将自动向Keycloak服务器发送有关Infinispan群集更改的新拓扑， 因此，Keycloak端的远程存储将知道它可以连接到哪个Infinispan服务器。阅读Infinispan和WildFly文档了解更多细节。
- 强烈建议在启动**任何**站点中的Keycloak服务器之前，在每个站点中运行一个主Infinispan服务器。 在我们的例子中，我们首先在所有Keycloak服务器之前启动了`jdg1`和`jdg2`。 如果您仍然需要运行Keycloak服务器并且备份站点处于脱机状态，建议您在站点上的Infinispan服务器上手动切换备份站点，如[使站点脱机并联机](https://www.keycloak.org/docs/latest/server_installation/index.html#onoffline)中所述。 如果不手动将不可用站点脱机，则第一次启动可能会失败，或者在启动期间可能会出现一些异常，直到备份站点因配置的失败操作计数而自动脱机。

#### 3.4.10. 使网站脱机和在线

例如，假设这种情况：

1. 站点`site2`从`site1`角度完全脱机。 这意味着`site2`上的所有Infinispan服务器都关闭**或者`site1`和`site2`之间的网络被破坏了。
2. 您在站点`site1`中运行Keycloak服务器和Infinispan服务器`jdg1`
3. 有人在`site1`上的Keycloak服务器上登录。
4. 来自`site1`的Keycloak服务器将尝试将会话写入`jdg1`服务器上的远程缓存，该服务器应该将数据备份到`site2`中的`jdg2`服务器。 有关详细信息，请参阅[通信详细信息](https://www.keycloak.org/docs/latest/server_installation/index.html#communication)。
5. 服务器`jdg2`离线或无法访问`jdg1`。 所以从`jdg1`到`jdg2`的备份将失败。
6. 在`jdg1`日志中抛出异常，故障也将从`jdg1`服务器传播到Keycloak服务器，因为配置了默认的`FAIL`备份失败策略。 有关备份策略的详细信息，请参阅[备份失败策略](https://www.keycloak.org/docs/latest/server_installation/index.html#backupfailure)。
7. 错误也会在Keycloak方面发生，用户可能无法完成登录。

根据您的环境，站点之间的网络可能或多或少可能不可用或暂时中断（裂脑）。如果发生这种情况，最好是`site1`上的Infinispan服务器知道`site2`上的Infinispan服务器不可用，因此它们将停止尝试访问`jdg2`站点上的服务器，并且不会发生备份故障。这就是所谓的“让网站脱机”。

使网站脱机

有两种方法可以使网站脱机。

**由管理员手动** - 管理员可以使用`jconsole`或其他工具运行一些JMX操作来手动使特定站点脱机。 这非常有用，尤其是在计划中断时。使用`jconsole`或CLI，您可以连接到`jdg1`服务器并使`site2`脱机。 有关这方面的更多详细信息，请参见[JDG文档](https://access.redhat.com/documentation/en-us/red_hat_data_grid/7.3/html/red_hat_data_grid_user_guide/x_site_replication#taking_a_site_offline)。

> 通常需要对[SYNC或ASYNC备份](https://www.keycloak.org/docs/latest/server_installation/index.html#backups)中提到的所有Keycloak缓存执行这些步骤。 

**自动** - 经过一定数量的失败备份后，`site2`通常会自动脱机。 这是通过在[Infinispan服务器设置](https://www.keycloak.org/docs/latest/server_installation/index.html#jdgsetup)中配置的缓存配置中的`take-offline`元素的配置来完成的。

```xml
<take-offline min-wait="60000" after-failures="3" />
```

这个示例显示，如果在60秒内至少有3个后续备份失败，并且没有任何成功备份，那么对于特定的单个缓存，站点将自动脱机。

自动使站点脱机是非常有用的，特别是当站点之间的网络中断是计划外的。 缺点是，在检测到网络中断之前，会有一些失败的备份，这也可能意味着应用程序端出现故障。 例如，某些用户的登录失败或登录超时很长。 特别是如果使用值为 `FAIL` 的 `failure-policy`。

> 每个缓存都会单独跟踪站点是否处于脱机状态。 

把网站在线

一旦你的网络恢复了，`site1` 和 `site2` 可以互相交谈，你可能需要把网站上线。这需要通过JMX或CLI手动完成，方法类似于使站点脱机。同样，您可能需要检查所有缓存并将它们联机。

一旦网站上线，通常最好:

- 做[状态转移](https://www.keycloak.org/docs/latest/server_installation/index.html#statetransfer)。
- 手动[清除缓存](https://www.keycloak.org/docs/latest/server_installation/index.html#clearcache)。

#### 3.4.11. 状态转移

国家转移是必需的手动步骤。 Infinispan服务器不会自动执行此操作，例如在裂脑期间，只有管理员可以决定哪个站点具有首选项，因此是否需要在两个站点之间双向进行状态转移，或者只是单向进行，如同仅来自`site1 `到`site2`，但不是从`site2`到`site1`。

双向状态转移将确保`site1`上的裂脑**后**创建的实体被转移到`site2`上。 这不是问题，因为它们在`site2`上还不存在。 类似地，在`site2`上裂脑**后**创建的实体将被转移到`site1`上。 可能有问题的部分是那些在两个站点上的裂脑**之前**存在并且在两个站点上的裂脑期间更新的实体。 当这种情况发生时，其中一个站点将**胜出**，并覆盖第二个站点在脑裂期间完成的更新。

不幸的是，没有任何通用的解决方案。 脑裂和网络中断只是状态，通常不可能100%正确地处理站点之间100%一致的数据。 就Keycloak而言，它通常不是一个关键问题。 在最坏的情况下，用户需要重新登录到他们的客户端，或者有不正确的loginFailures计数用于强力保护。 有关如何处理裂脑的更多提示，请参阅Infinispan/JGroups文档。

状态转移也可以通过JMX在Infinispan服务器端完成。 操作名称是`pushState`。 几乎没有其他操作来监视状态，取消推送状态等。 有关状态转移的更多信息，请参阅[Infinispan docs](https://access.redhat.com/documentation/en-us/red_hat_data_grid/7.3/html/red_hat_data_grid_user_guide/x_site_replication#pushing_state_transfer_to_sites)。

#### 3.4.12. 清除缓存

在脑裂之后，可以安全地在Keycloak管理控制台中手动清除缓存。 这是因为在`site1`上的数据库中可能存在一些数据发生了变化，并且由于该事件，缓存应该被无效，并且在脑裂期间没有被转移到`site2`。 因此，`site2`上的Keycloak节点可能仍然在其缓存中有一些陈旧的数据。

要清除缓存，请参阅[清除服务器缓存](https://www.keycloak.org/docs/6.0/server_admin/#_clear-cache)。

当网络恢复时，仅在任何随机站点上的一个Keycloak节点上清除缓存就足够了。 缓存失效事件将发送到所有站点中的所有其他Keycloak节点。 但是，需要对所有缓存（领域，用户，密钥）执行此操作。 有关详细信息，请参阅[清除服务器缓存](https://www.keycloak.org/docs/6.0/server_admin/#_clear-cache)。

#### 3.4.13. 调整JDG缓存配置

本节包含配置JDG缓存的技巧和选项。

备份失败策略

默认情况下，JDG的`clustered.xml`文件中Infinispan缓存配置中的备份`failure-policy`配置级别为`FAIL`。 您可以根据需要将其更改为`WARN`或`IGNORE`。

`FAIL`和`WARN`之间的区别在于，当使用`FAIL`并且Infinispan服务器尝试将数据备份到另一个站点并且备份失败时，失败将传播回调用者（Keycloak服务器）。 备份可能会失败，因为第二个站点暂时无法访问，或者存在尝试更新同一实体的并发事务。 在这种情况下，Keycloak服务器将重试该操作几次。 但是，如果重试失败，则用户可能会在更长的超时后看到错误。

使用`WARN`时，失败的备份不会从Infinispan服务器传播到Keycloak服务器。 用户将看不到错误，将忽略失败的备份。 将有一个较短的超时，通常为10秒，因为这是备份的默认超时。 它可以通过`backup`元素的属性`timeout`来改变。 不会重试。 Infinispan服务器日志中只会出现一条WARNING消息。

潜在的问题是，在某些情况下，站点之间可能只有一些短暂的网络中断，其中重试（使用`FAIL`策略）可能有所帮助，因此使用`WARN`（不重试），将会有 站点间的一些数据不一致。 如果尝试在两个站点上同时更新同一实体，也会发生这种情况。

这些不一致有多糟糕？ 通常只表示用户需要重新进行身份验证。

当使用`WARN`策略时，可能会发生由`actionTokens`缓存提供并处理该特定密钥的一次性缓存实际上是单独使用，但可能“成功”两次写入相同的密钥。 但是，例如，OAuth2规范[提及](https://tools.ietf.org/html/rfc6749#section-10.5)该代码必须是单一使用的。 使用`WARN`策略，可能无法严格保证，如果尝试在两个站点中同时写入相同的代码，则可以写两次相同的代码。

如果有较长的网络中断或裂脑，那么同时使用`FAIL`和`WARN`，其他站点将在一段时间后失效，如[使站点离线和联机](https://www.keycloak.org/docs/latest/server_installation/index.html#onoffline)中所述。 使用默认的1分钟超时，通常需要1-3分钟才能使所有相关的缓存脱机。 之后，从最终用户的角度来看，所有操作都可以正常工作。 如[网站离线和在线](https://www.keycloak.org/docs/latest/server_installation/index.html#onoffline)中所述，您只需在网站重新联机时手动恢复该网站。

总之，如果您希望站点之间频繁，更长时间的中断，并且您可以接受一些数据不一致且不是100％准确的一次性缓存，但您绝不希望最终用户看到错误和长时间超时，那么 切换到`WARN`。

`WARN`和`IGNORE`之间的区别在于，`IGNORE`警告不会写入JDG日志中。 请参阅Infinispan文档中的更多详细信息。

Lock acquisition timeout (锁定获取超时)

默认配置是在NON_DURABLE_XA模式下使用事务，获取超时为0.这意味着如果同一个密钥正在进行另一个事务，则事务将快速失败。

将其切换为0而不是默认10秒的原因是为了避免可能的死锁问题。 使用Keycloak，可能会发生同一实体（通常是会话实体或loginFailure）从两个站点同时更新。 这可能会在某些情况下导致死锁，这将导致事务被阻止10秒。 有关详细信息，请参阅[此JIRA报告](https://issues.jboss.org/browse/JDG-1318)。

如果超时为0，则事务将立即失败，如果配置了具有值`FAIL`的备份`failure-policy`，则将从Keycloak重试该事务。 只要第二个并发事务完成，重试通常就会成功，并且实体将从两个并发事务中应用更新。

我们看到使用此配置进行并发事务的一致性和结果非常好，建议保留它。

唯一（非功能）问题是Infinispan服务器日志中的异常，每次锁定不可用时都会发生。

#### 3.4.14. 同步或异步备份

`backup`元素的一个重要部分是`strategy`属性。 您必须决定是否需要`SYNC`或`ASYNC`。 我们有7个可能支持跨数据中心复制的缓存，这些缓存可以配置为3种不同的交叉直流模式：

1. 同步 备份
2. 异步 备份
3. 不备份

如果使用`SYNC`备份，则备份是同步的，并且在第二个站点上处理备份后，调用者（Keycloak服务器）端的操作将被视为已完成。 这比`ASYNC`的性能更差，但另一方面，您确信在`site2`上对特定实体（如用户会话）的后续读取将从`site1`中看到更新。 此外，如果您想要数据一致性，则需要它。 与`ASYNC`一样，如果备份到其他站点失败，则不会通知呼叫者。

对于某些缓存，甚至可能根本不进行备份并完全跳过将数据写入Infinispan服务器。 要进行此设置，请不要将`remote-store`元素用于Keycloak端的特定缓存 (文件 `KEYCLOAK_HOME/standalone/configuration/standalone-ha.xml`) ，然后使用特定的`replicated-cache`元素。 在Infinispan服务器端也不需要。

默认情况下，所有7个缓存都配置了`SYNC`备份，这是最安全的选项。 以下是一些需要考虑的事项：

- 如果您使用的是主动/被动模式（所有Keycloak服务器都在单站点`site1`中，而`site2`中的Infinispan服务器仅用作备份。请参阅[模式](https://www.keycloak.org/docs/latest/server_installation/index.html#modes)以获取更多详细信息），然后通常可以使用所有缓存的`ASYNC`策略来保存性能。
- `work`缓存主要用于向其他站点发送一些消息，例如缓存失效事件。 它还用于确保某些特殊事件,例如userStorage同步;仅在单个站点上发生。 建议将此设置保持为`SYNC。
- `actionTokens`缓存用作一次性缓存，用于跟踪某些令牌/票证仅使用一次。 例如，动作令牌或OAuth2代码。 可以将其设置为`ASYNC`以略微提高性能，但是不能保证特定票证真的是单次使用。 例如，如果两个站点中同时存在同一票证请求，那么两个请求都可能成功使用`ASYNC`策略。 所以你在这里设置的将取决于你是否更喜欢更好的安全性（`SYNC`策略）或更好的性能（`ASYNC`策略）。
- `loginFailures`缓存可以在3种模式中的任何一种中使用。 如果根本没有备份，则意味着每个站点将单独计算用户的登录失败次数（请参阅[Infinispan缓存](https://www.keycloak.org/docs/latest/server_installation/index.html#cache)了解详情）。 这具有一些安全隐患，但它具有一些性能优势。 此外，它还可以降低拒绝服务（DoS）攻击的风险。 例如，如果攻击者使用两个站点上的用户的用户名和密码模拟1000个并发请求，则意味着在站点之间传递大量消息，这可能导致网络拥塞。 `ASYNC`策略可能更糟糕，因为等待备份到其他站点不会阻止攻击者请求，从而导致可能更加拥挤的网络流量。 使用`ASYNC`策略，登录失败的次数也不准确。

对于数据中心之间网络速度较慢且DoS概率较低的环境，建议不要备份`loginFailures`缓存。

- 建议在`SYNC`中保留`sessions`和`clientSessions`缓存。 只有当您确定用户请求和反向通道请求（从[请求处理](https://www.keycloak.org/docs/latest/server_installation/index.html#requestprocessing)中描述的客户端应用程序到Keycloak的请求）时，才可以将它们切换为`ASYNC`）将始终在同一站点上处理。 例如，如果：

  - 您使用主动/被动模式，如[模式](https://www.keycloak.org/docs/latest/server_installation/index.html#modes)所述。

  - 您的所有客户端应用程序都使用Keycloak [JavaScript Adapter](https://www.keycloak.org/docs/latest/securing_apps/index.html#_javascript_adapter)。 JavaScript适配器在浏览器中发送反向通道请求，因此它们参与浏览器粘性会话，并将在与该用户的其他浏览器请求相同的群集节点（因此在同一站点上）结束。

  - 您的负载均衡器能够根据客户端IP地址（位置）提供请求，并在两个站点上部署客户端应用程序。

    例如，您有2个站点LON和NYC。 只要您的应用程序也部署在LON和NYC站点中，您就可以确保来自伦敦用户的所有用户请求将被重定向到LON站点中的应用程序以及LON站点中的Keycloak服务器。 来自LON站点客户端部署的Backchannel请求也将在LON站点中的Keycloak服务器上结束。 另一方面，对于美国用户，所有Keycloak请求，应用程序请求和反向通道请求将在NYC站点上处理。

- 对于`offlineSessions`和`offlineClientSessions`，它是相似的，不同之处在于，如果您从未计划为任何客户端应用程序使用脱机令牌，则根本不需要备份它们。

一般来说，如果您有疑问并且性能不适合您，那么将缓存保持在`SYNC`策略中会更安全。

> 关于切换到SYNC/ASYNC备份，请确保编辑`backup`元素的`strategy`属性。 例如这样： 

```xml
<backup site="site2" failure-policy="FAIL" strategy="ASYNC" enabled="true">
```

注意cache-configuration元素的`mode`属性。

#### 3.4.15. 故障排除

以下提示旨在帮助您解决问题：

- 建议通过[基本设置](https://www.keycloak.org/docs/latest/server_installation/index.html#setup)并首先使用此工具，以便您对事情有所了解 工作。 阅读整篇文档以了解事物也是明智之举。

- 按照[Infinispan服务器设置](https://www.keycloak.org/docs/latest/server_installation/index.html#jdgsetup)中的说明检入Infinispan的jconsole集群状态（GMS）和JGroups状态（RELAY）。 如果事情看起来不像预期，那么问题很可能出在Infinispan服务器的设置上。

- 对于Keycloak服务器，您应该在服务器启动期间看到这样的消息：

  ```
  18:09:30,156 INFO  [org.keycloak.connections.infinispan.DefaultInfinispanConnectionProviderFactory] (ServerService Thread Pool -- 54)
  Node name: node11, Site name: site1
  ```

  在Keycloak服务器启动期间检查站点名称和节点名称是否与预期一致。

- 检查Keycloak服务器是否按预期位于集群中，包括只有来自同一数据中心的Keycloak服务器彼此在集群中。 这也可以通过GMS视图在JConsole中进行检查。 有关其他详细信息，请参阅[集群疑难解答](https://www.keycloak.org/docs/6.0/server_installation/#troubleshooting)。

- 如果在启动Keycloak服务器期间有异常，如下所示：

  ```
  17:33:58,605 ERROR [org.infinispan.client.hotrod.impl.operations.RetryOnFailureOperation] (ServerService Thread Pool -- 59) ISPN004007: Exception encountered. Retry 10 out of 10: org.infinispan.client.hotrod.exceptions.TransportException:: Could not fetch transport
  ...
  Caused by: org.infinispan.client.hotrod.exceptions.TransportException:: Could not connect to server: 127.0.0.1:12232
  	at org.infinispan.client.hotrod.impl.transport.tcp.TcpTransport.<init>(TcpTransport.java:82)
  ```

  它通常意味着Keycloak服务器无法访问自己的数据中心的Infinispan服务器。 确保按预期设置防火墙，并且可以连接Infinispan服务器。

- 如果在启动Keycloak服务器期间有异常，如下所示：

  ```
  16:44:18,321 WARN  [org.infinispan.client.hotrod.impl.protocol.Codec21] (ServerService Thread Pool -- 57) ISPN004005: Error received from the server: javax.transaction.RollbackException: ARJUNA016053: Could not commit transaction.
   ...
  ```

  然后检查您站点的相应Infinispan服务器的日志，并检查是否未能备份到其他站点。 如果备份站点不可用，则建议将其切换为脱机，以便Infinispan服务器不会尝试备份到脱机站点，从而导致操作也在Keycloak服务器端成功通过。 有关详细信息，请参阅[交叉DC部署管理](https://www.keycloak.org/docs/latest/server_installation/index.html#administration)。

- 检查可通过JMX获得的Infinispan统计信息。 例如，尝试登录，然后查看新会话是否已成功写入两个Infinispan服务器，并在那里的`sessions`缓存中可用。 这可以通过检查MBean的`sessions`缓存中的元素数来间接完成`jboss.datagrid-infinispan:type=Cache,name="sessions(repl_sync)",manager="clustered",component=Statistics`和属性`numberOfEntries`。 登录后，两个站点上的两个Infinispan服务器上都应该有一个`numberOfEntries`条目。

- 按照[Keycloak服务器设置](https://www.keycloak.org/docs/latest/server_installation/index.html#serversetup)所述启用DEBUG日志记录。 例如，如果您登录并且认为新会话在第二个站点上不可用，则最好检查Keycloak服务器日志并检查是否按照[Keycloak服务器设置](https://www.keycloak.org/docs/latest/server_installation/index.html#serversetup)中的说明触发了侦听器。 如果您不知道并想要在keycloak-user邮件列表上询问，那么从电子邮件中的两个数据中心的Keycloak服务器发送日志文件会很有帮助。 将日志片段添加到邮件或将日志放在某处并在电子邮件中引用它们。

- 如果您在`site1`上的Keycloak服务器上更新了实体，例如`user`，并且您没有在`site2`上的Keycloak服务器上看到该实体更新，那么问题可能在于复制同步数据库本身 或Keycloak缓存未正确无效。 您可以尝试暂时禁用Keycloak缓存，如[here](https://www.keycloak.org/docs/6.0/server_installation/#disabling-caching)所述，以确定问题是否在数据库复制级别。 此外，手动连接到数据库并检查数据是否按预期更新可能会有所帮助。 这是特定于每个数据库的，因此您需要查阅数据库的文档。

- 有时您可能会在Infinispan服务器日志中看到与此类锁相关的异常：

  ```
  (HotRodServerHandler-6-35) ISPN000136: Error executing command ReplaceCommand,
  writing keys [[B0x033E243034396234..[39]]: org.infinispan.util.concurrent.TimeoutException: ISPN000299: Unable to acquire lock after
  0 milliseconds for key [B0x033E243034396234..[39] and requestor GlobalTx:jdg1:4353. Lock is held by GlobalTx:jdg1:4352
  ```

  这些例外不一定是个问题。 它们可能在任何时候在两个DC上触发同一实体的并发编辑时发生。 这在部署中很常见。 通常，Keycloak服务器会收到有关失败操作的通知，并会重试，因此从用户的角度来看，通常没有任何问题。

- 如果Keycloak服务器启动期间有异常，如下所示：

  ```
  16:44:18,321 WARN  [org.infinispan.client.hotrod.impl.protocol.Codec21] (ServerService Thread Pool -- 55) ISPN004005: Error received from the server: java.lang.SecurityException: ISPN000287: Unauthorized access: subject 'Subject with principal(s): []' lacks 'READ' permission
   ...
  ```

  这些日志条目是Keycloak自动检测Infinispan是否需要身份验证的结果，并且意味着需要进行身份验证。 此时您将注意到服务器成功启动并且您可以安全地忽略这些或服务器无法启动。 如果服务器无法启动，请确保已按照[Infinispan服务器设置](https://www.keycloak.org/docs/latest/server_installation/index.html#jdgsetup)中的说明正确配置Infinispan进行身份验证。 要防止包含此日志条目，可以通过在`spi=connectionsInfinispan/provider=default`配置中将`remoteStoreSecurityEnabled`属性设置为`true`来强制进行身份验证：

  ```xml
  <subsystem xmlns="urn:jboss:domain:keycloak-server:1.1">
      ...
      <spi name="connectionsInfinispan">
          ...
          <provider name="default" enabled="true">
              <properties>
                  ...
                  <property name="remoteStoreSecurityEnabled" value="true"/>
              </properties>
          </provider>
      </spi>
  ```

- 如果您尝试使用Keycloak对您的应用程序进行身份验证，但身份验证失败并且浏览器中存在无限次重定向，并且您在Keycloak服务器日志中看到如下错误：

  ```
  2017-11-27 14:50:31,587 WARN  [org.keycloak.events] (default task-17) type=LOGIN_ERROR, realmId=master, clientId=null, userId=null, ipAddress=aa.bb.cc.dd, error=expired_code, restart_after_timeout=true
  ```

  这可能意味着您的负载均衡器需要设置为支持粘性会话。 确保在启动Keycloak服务器（Property`jboss.node.name`）期间使用的提供的路由名称包含负载均衡器服务器用于标识当前服务器的正确名称。

- 如果Infinispan`work`缓存无限增长，您可能会遇到[此Infinispan问题](https://issues.jboss.org/browse/JDG-987)，这是由缓存项未正确过期引起的。 在这种情况下，使用空的`<expiration />`标记更新缓存声明，如下所示：

  ```xml
      <replicated-cache name="work" configuration="sessions-cfg">
          <expiration />
      </replicated-cache>
  ```

- 如果您在Infinispan服务器日志中看到警告，例如：

  ```
  18:06:19,687 WARN  [org.infinispan.server.hotrod.Decoder2x] (HotRod-ServerWorker-7-12) ISPN006011: Operation 'PUT_IF_ABSENT' forced to
    return previous value should be used on transactional caches, otherwise data inconsistency issues could arise under failure situations
  18:06:19,700 WARN  [org.infinispan.server.hotrod.Decoder2x] (HotRod-ServerWorker-7-10) ISPN006010: Conditional operation 'REPLACE_IF_UNMODIFIED' should
    be used with transactional caches, otherwise data inconsistency issues could arise under failure situations
  ```

  你可以忽略它们。 为了避免警告，Infinispan服务器端的缓存可以更改为事务缓存，但不推荐这样做，因为它可能导致由bug引起的其他一些问题<https://issues.jboss.org/browse/ISPN-9323>。 所以现在，警告只需要被忽略。

- 如果您在Infinispan服务器日志中看到错误，例如：

  ```
  12:08:32,921 ERROR [org.infinispan.server.hotrod.CacheDecodeContext] (HotRod-ServerWorker-7-11) ISPN005003: Exception reported: org.infinispan.server.hotrod.InvalidMagicIdException: Error reading magic byte or message id: 7
  	at org.infinispan.server.hotrod.HotRodDecoder.readHeader(HotRodDecoder.java:184)
  	at org.infinispan.server.hotrod.HotRodDecoder.decodeHeader(HotRodDecoder.java:133)
  	at org.infinispan.server.hotrod.HotRodDecoder.decode(HotRodDecoder.java:92)
  	at io.netty.handler.codec.ByteToMessageDecoder.callDecode(ByteToMessageDecoder.java:411)
  	at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:248)
  ```

  并且您在Keycloak日志中看到一些类似的错误，它可以表明正在使用的HotRod协议的版本不兼容。 当您尝试将Keycloak与JDG 7.2服务器或旧版本的Infinispan服务器一起使用时，可能会发生这种情况。 如果将`protocolVersion`属性作为附加属性添加到Keycloak配置文件中的`remote-store`元素，将会有所帮助。 例如：

  ```
  <property name="protocolVersion">2.6</property>
  ```

## 4. 管理子系统配置

Keycloak的低级配置是通过编辑发行版中的`standalone.xml`，`standalone-ha.xml`或`domain.xml`文件来完成的。 此文件的位置取决于您的[操作模式](https://www.keycloak.org/docs/latest/server_installation/index.html#_operating-mode)。

虽然您可以在此配置无限设置，但本节将重点介绍 *keycloak-server* 子系统的配置。 无论您使用哪个配置文件，*keycloak-server* 子系统的配置都是相同的。

keycloak-server子系统通常在文件末尾声明，如下所示：

```xml
<subsystem xmlns="urn:jboss:domain:keycloak-server:1.1">
   <web-context>auth</web-context>
   ...
</subsystem>
```

请注意，在重新启动服务器之前，此子系统中的任何更改都不会生效。

### 4.1. 配置SPI提供程序

每个配置设置的细节在该设置的上下文中的其他地方讨论。 但是，了解用于在SPI提供程序上声明设置的格式很有用。

Keycloak是一个高度模块化的系统，具有很大的灵活性。 有超过50个服务提供程序接口（SPI），您可以交换每个SPI的实现。 SPI的实现称为*提供者*。

SPI声明中的所有元素都是可选的，但完整的SPI声明如下所示：

```xml
<spi name="myspi">
    <default-provider>myprovider</default-provider>
    <provider name="myprovider" enabled="true">
        <properties>
            <property name="foo" value="bar"/>
        </properties>
    </provider>
    <provider name="mysecondprovider" enabled="true">
        <properties>
            <property name="foo" value="foo"/>
        </properties>
    </provider>
</spi>
```

这里我们为SPI`myspi`定义了两个提供程序。 `default-provider`被列为`myprovider`。 但是由SPI来决定如何处理这个设置。 有些SPI允许多个提供商，有些则不允许。 所以`default-provider`可以帮助SPI选择。

另请注意，每个提供程序都定义了自己的一组配置属性。 上面两个提供商都有一个名为`foo`的属性这一事实只是巧合。

每个属性值的类型由提供程序解释。 但是，有一个例外。 考虑`eventsStore`  SPI 的 `jpa` 提供程序：

```xml
<spi name="eventsStore">
    <provider name="jpa" enabled="true">
        <properties>
            <property name="exclude-events" value="[&quot;EVENT1&quot;,
                                                    &quot;EVENT2&quot;]"/>
        </properties>
    </provider>
</spi>
```

我们看到值以方括号开头和结尾。 这意味着该值将作为列表传递给提供程序。 在此示例中，系统将向提供程序传递一个包含两个元素值 *EVENT1* 和 *EVENT2* 的列表。 要向列表中添加更多值，只需使用逗号分隔每个列表元素即可。 不幸的是，你需要使用 `＆quot;` 来转义每个列表元素周围的引号。

### 4.2. 启动WildFly CLI

除了手动编辑配置外，您还可以通过 *jboss-cli* 工具发出命令来更改配置。 CLI允许您在本地或远程配置服务器。 当与脚本结合使用时，它尤其有用。

要启动WildFly CLI，您需要运行`jboss-cli`。

Linux/Unix

```bash
$ .../bin/jboss-cli.sh
```

Windows

```bat
> ...\bin\jboss-cli.bat
```

这将带您到这样的提示：

提示

```
[disconnected /]
```

如果您希望在正在运行的服务器上执行命令，则首先执行`connect`命令。

连接

```
[disconnected /] connect
connect
[standalone@localhost:9990 /]
```

你可能会想，“我没有输入任何用户名或密码！”。 如果您在运行独立服务器或域控制器的同一台计算机上运行`jboss-cli`，并且您的帐户具有适当的文件权限，则无需设置或输入管理员用户名和密码。 请参阅[* WildFly 16文档*](http://docs.wildfly.org/16/Admin_Guide.html)，了解如果您对该设置感到不舒服时如何使事情更安全的更多详细信息。

### 4.3. CLI嵌入式模式

如果您碰巧与独立服务器位于同一台计算机上，并且您希望在服务器未处于活动状态时发出命令，则可以将服务器嵌入CLI并在不允许传入请求的特殊模式下进行更改。 为此，首先使用您要更改的配置文件执行`embed-server`命令。

embed-server (嵌入服务器)

```
[disconnected /] embed-server --server-config=standalone.xml
[standalone@embedded /]
```

### 4.4. CLI GUI模式

CLI也可以在GUI模式下运行。 GUI模式启动Swing应用程序，允许您以图形方式查看和编辑 *running* 服务器的整个管理模型。 当您需要帮助格式化CLI命令并了解可用选项时，GUI模式特别有用。 GUI还可以从本地或远程服务器检索服务器日志。

从GUI模式开始

```bash
$ .../bin/jboss-cli.sh --gui
```

*注意: 要连接到远程服务器，还要传递--connect选项。 使用--help选项可获取更多详细信息。*

启动GUI模式后，您可能需要向下滚动才能找到节点 `subsystem=keycloak-server` 。 如果右键单击该节点并单击  `Explore subsystem=keycloak-server` ，您将获得一个仅显示keycloak-server子系统的新选项卡。

![cli gui](assets/cli-gui.png)

### 4.5. CLI脚本

CLI具有广泛的脚本功能。 脚本只是一个包含CLI命令的文本文件。 考虑一个关闭主题和模板缓存的简单脚本。

turn-off-caching.cli

```
/subsystem=keycloak-server/theme=defaults/:write-attribute(name=cacheThemes,value=false)
/subsystem=keycloak-server/theme=defaults/:write-attribute(name=cacheTemplates,value=false)
```

要执行脚本，我可以按照CLI GUI中的`Scripts`菜单，或者从命令行执行脚本，如下所示：

```bash
$ .../bin/jboss-cli.sh --file=turn-off-caching.cli
```

### 4.6. CLI食谱

以下是一些配置任务以及如何使用CLI命令执行它们。 请注意，在第一个示例中的所有示例中，我们使用通配符路径 `**` 表示您应该替换keycloak-server子系统的路径。

对于独立模式，这意味着:

```
**` = `/subsystem=keycloak-server
```

对于域模式，这意味着：

```
**` = `/profile=auth-server-clustered/subsystem=keycloak-server
```

#### 4.6.1. 更改服务器的Web上下文

```
/subsystem=keycloak-server/:write-attribute(name=web-context,value=myContext)
```

#### 4.6.2. 设置全局默认主题

```
**/theme=defaults/:write-attribute(name=default,value=myTheme)
```

#### 4.6.3. 添加新的SPI和提供程序

```
**/spi=mySPI/:add
**/spi=mySPI/provider=myProvider/:add(enabled=true)
```

#### 4.6.4. 禁用提供商

```
**/spi=mySPI/provider=myProvider/:write-attribute(name=enabled,value=false)
```

#### 4.6.5. 更改SPI的默认提供程序

```
**/spi=mySPI/:write-attribute(name=default-provider,value=myProvider)
```

#### 4.6.6. 配置dblock SPI

```
**/spi=dblock/:add(default-provider=jpa)
**/spi=dblock/provider=jpa/:add(properties={lockWaitTimeout => "900"},enabled=true)
```

#### 4.6.7. 为提供者添加或更改单个属性值

```
**/spi=dblock/provider=jpa/:map-put(name=properties,key=lockWaitTimeout,value=3)
```

#### 4.6.8. 从提供程序中删除单个属性

```
**/spi=dblock/provider=jpa/:map-remove(name=properties,key=lockRecheckTime)
```

#### 4.6.9. 在类型为`List`的提供程序属性上设置值

```
**/spi=eventsStore/provider=jpa/:map-put(name=properties,key=exclude-events,value=[EVENT1,EVENT2])
```

## 5. Profiles (特征文件)

Keycloak中的某些功能默认情况下未启用，这些功能包括不完全支持的功能。 此外，默认情况下会启用一些功能，但可以禁用这些功能。

可以启用和禁用的功能包括：

| 名称                     | 描述                                  | 默认启用 | 支持级别 |
| :----------------------- | :------------------------------------------- | :----------------- | :------------ |
| account2                 | New Account Management Console               | No                 | Experimental  |
| account_api              | Account Management REST API                  | No                 | Preview       |
| admin_fine_grained_authz | Fine-Grained Admin Permissions               | No                 | Preview       |
| authz_drools_policy      | Drools Policy for Authorization Services     | No                 | Preview       |
| docker                   | Docker Registry protocol                     | No                 | Supported     |
| impersonation            | Ability for admins to impersonate users      | Yes                | Supported     |
| openshift_integration    | Extension to enable securing OpenShift       | No                 | Preview       |
| script                   | Write custom authenticators using JavaScript | Yes                | Preview       |
| token_exchange           | Token Exchange Service                       | No                 | Preview       |

要启用所有预览功能，请启动服务器：

```bash
bin/standalone.sh|bat -Dkeycloak.profile=preview
```

您可以通过在域模式下为`server-one`创建文件`standalone/configuration/profile.properties`（或`domain/servers/server-one/configuration/profile.properties`）来永久设置它。 将以下内容添加到文件中：

```properties
profile=preview
```

要启用特定功能，请启动服务器：

```bash
bin/standalone.sh|bat -Dkeycloak.profile.feature.<feature name>=enabled
```

例如，要启用Docker，请使用`-Dkeycloak.profile.feature.docker=enabled`。

您可以通过添加以下内容在`profile.properties`文件中永久设置它：

```properties
feature.docker=enabled
```

要禁用特定功能，请启动服务器：

```bash
bin/standalone.sh|bat -Dkeycloak.profile.feature.<feature name>=disabled
```

例如，要禁用模拟，请使用`-Dkeycloak.profile.feature.impersonation=disabled`。

您可以通过添加以下内容在`profile.properties`文件中永久设置它：

```properties
feature.impersonation=disabled
```

## 6. 关系数据库设置

Keycloak附带了自己的基于Java的嵌入式关系数据库H2。 这是Keycloak用于保存数据的默认数据库，以便您可以开箱即用地运行身份验证服务器。 我们强烈建议您使用更多生产就绪的外部数据库替换它。 H2数据库在高并发情况下不是很可行，也不应该在集群中使用。 本章的目的是向您展示如何将Keycloak连接到更成熟的数据库。

Keycloak使用两种分层技术来持久保存其关系数据。 底层技术是JDBC。 JDBC是一种用于连接到RDBMS的Java API。 每种数据库类型都有不同的JDBC驱动程序，由数据库供应商提供。 本章讨论如何配置Keycloak以使用这些特定于供应商的驱动程序之一。

用于持久性的顶层技术是Hibernate JPA。 这是将Java对象映射到关系数据的关系映射API的对象。 Keycloak的大部分部署永远不会触及Hibernate的配置方面，但我们将讨论如果遇到这种罕见的情况，如何做到这一点。

> 在 *WildFly 16文档* 的[数据源配置章节](http://docs.wildfly.org/16/Admin_Guide.html#DataSource)中更全面地介绍了数据源配置。

### 6.1. RDBMS设置清单

以下是为Keycloak配置RDBMS所需执行的步骤。

1. Locate and download a JDBC driver for your database
2. Package the driver JAR into a module and install this module into the server
3. Declare the JDBC driver in the configuration profile of the server
4. Modify the datasource configuration to use your database’s JDBC driver
5. Modify the datasource configuration to define the connection parameters to your database

This chapter will use PostgresSQL for all its examples. Other databases follow the same steps for installation.

### 6.2. Package the JDBC Driver

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/database/jdbc.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/database/jdbc.adoc)

Find and download the JDBC driver JAR for your RDBMS. Before you can use this driver, you must package it up into a module and install it into the server. Modules define JARs that are loaded into the Keycloak classpath and the dependencies those JARs have on other modules. They are pretty simple to set up.

Within the *…/modules/* directory of your Keycloak distribution, you need to create a directory structure to hold your module definition. The convention is use the Java package name of the JDBC driver for the name of the directory structure. For PostgreSQL, create the directory *org/postgresql/main*. Copy your database driver JAR into this directory and create an empty *module.xml* file within it too.

Module Directory

![db module](assets/db-module.png)

After you have done this, open up the *module.xml* file and create the following XML:

Module XML

```
<?xml version="1.0" ?>
<module xmlns="urn:jboss:module:1.3" name="org.postgresql">

    <resources>
        <resource-root path="postgresql-9.4.1212.jar"/>
    </resources>

    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```

The module name should match the directory structure of your module. So, *org/postgresql* maps to `org.postgresql`. The `resource-root path` attribute should specify the JAR filename of the driver. The rest are just the normal dependencies that any JDBC driver JAR would have.

### 6.3. Declare and Load JDBC Driver

The next thing you have to do is declare your newly packaged JDBC driver into your deployment profile so that it loads and becomes available when the server boots up. Where you perform this action depends on your [operating mode](https://www.keycloak.org/docs/latest/server_installation/index.html#_operating-mode). If you’re deploying in standard mode, edit *…/standalone/configuration/standalone.xml*. If you’re deploying in standard clustering mode, edit *…/standalone/configuration/standalone-ha.xml*. If you’re deploying in domain mode, edit *…/domain/configuration/domain.xml*. In domain mode, you’ll need to make sure you edit the profile you are using: either `auth-server-standalone` or `auth-server-clustered`

Within the profile, search for the `drivers` XML block within the `datasources` subsystem. You should see a pre-defined driver declared for the H2 JDBC driver. This is where you’ll declare the JDBC driver for your external database.

JDBC Drivers

```
  <subsystem xmlns="urn:jboss:domain:datasources:5.0">
     <datasources>
       ...
       <drivers>
          <driver name="h2" module="com.h2database.h2">
              <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
          </driver>
       </drivers>
     </datasources>
  </subsystem>
```

Within the `drivers` XML block you’ll need to declare an additional JDBC driver. It needs to have a `name` which you can choose to be anything you want. You specify the `module` attribute which points to the `module` package you created earlier for the driver JAR. Finally you have to specify the driver’s Java class. Here’s an example of installing PostgreSQL driver that lives in the module example defined earlier in this chapter.

Declare Your JDBC Drivers

```
  <subsystem xmlns="urn:jboss:domain:datasources:5.0">
     <datasources>
       ...
       <drivers>
          <driver name="postgresql" module="org.postgresql">
              <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
          </driver>
          <driver name="h2" module="com.h2database.h2">
              <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
          </driver>
       </drivers>
     </datasources>
  </subsystem>
```

### 6.4. Modify the Keycloak Datasource

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/database/datasource.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/database/datasource.adoc)

After declaring your JDBC driver, you have to modify the existing datasource configuration that Keycloak uses to connect it to your new external database. You’ll do this within the same configuration file and XML block that you registered your JDBC driver in. Here’s an example that sets up the connection to your new database:

Declare Your JDBC Drivers

```
  <subsystem xmlns="urn:jboss:domain:datasources:5.0">
     <datasources>
       ...
       <datasource jndi-name="java:jboss/datasources/KeycloakDS" pool-name="KeycloakDS" enabled="true" use-java-context="true">
           <connection-url>jdbc:postgresql://localhost/keycloak</connection-url>
           <driver>postgresql</driver>
           <pool>
               <max-pool-size>20</max-pool-size>
           </pool>
           <security>
               <user-name>William</user-name>
               <password>password</password>
           </security>
       </datasource>
        ...
     </datasources>
  </subsystem>
```

Search for the `datasource` definition for `KeycloakDS`. You’ll first need to modify the `connection-url`. The documentation for your vendor’s JDBC implementation should specify the format for this connection URL value.

Next define the `driver` you will use. This is the logical name of the JDBC driver you declared in the previous section of this chapter.

It is expensive to open a new connection to a database every time you want to perform a transaction. To compensate, the datasource implementation maintains a pool of open connections. The `max-pool-size` specifies the maximum number of connections it will pool. You may want to change the value of this depending on the load of your system.

Finally, with PostgreSQL at least, you need to define the database username and password that is needed to connect to the database. You may be worried that this is in clear text in the example. There are methods to obfuscate this, but this is beyond the scope of this guide.

|      | For more information about datasource features, see [the datasource configuration chapter](http://docs.wildfly.org/16/Admin_Guide.html#DataSource) in the *WildFly 16 Documentation*. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 6.5. Database Configuration

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/database/hibernate.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/database/hibernate.adoc)

The configuration for this component is found in the `standalone.xml`, `standalone-ha.xml`, or `domain.xml` file in your distribution. The location of this file depends on your [operating mode](https://www.keycloak.org/docs/latest/server_installation/index.html#_operating-mode).

Database Config

```
<subsystem xmlns="urn:jboss:domain:keycloak-server:1.1">
    ...
    <spi name="connectionsJpa">
     <provider name="default" enabled="true">
         <properties>
             <property name="dataSource" value="java:jboss/datasources/KeycloakDS"/>
             <property name="initializeEmpty" value="false"/>
             <property name="migrationStrategy" value="manual"/>
             <property name="migrationExport" value="${jboss.home.dir}/keycloak-database-update.sql"/>
         </properties>
     </provider>
    </spi>
    ...
</subsystem>
```

Possible configuration options are:

- dataSource

  JNDI name of the dataSource

- jta

  boolean property to specify if datasource is JTA capable

- driverDialect

  Value of database dialect. In most cases you don’t need to specify this property as dialect will be autodetected by Hibernate.

- initializeEmpty

  Initialize database if empty. If set to false the database has to be manually initialized. If you want to manually initialize the database set migrationStrategy to `manual` which will create a file with SQL commands to initialize the database. Defaults to true.

- migrationStrategy

  Strategy to use to migrate database. Valid values are `update`, `manual` and `validate`. Update will automatically migrate the database schema. Manual will export the required changes to a file with SQL commands that you can manually execute on the database. Validate will simply check if the database is up-to-date.

- migrationExport

  Path for where to write manual database initialization/migration file.

- showSql

  Specify whether Hibernate should show all SQL commands in the console (false by default). This is very verbose!

- formatSql

  Specify whether Hibernate should format SQL commands (true by default)

- globalStatsInterval

  Will log global statistics from Hibernate about executed DB queries and other things. Statistics are always reported to server log at specified interval (in seconds) and are cleared after each report.

- schema

  Specify the database schema to use

|      | These configuration switches and more are described in the [*WildFly 16 Development Guide*](http://docs.wildfly.org/16/Developer_Guide.html#hibernate-properties). |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 6.6. Unicode Considerations for Databases

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/database/unicode-considerations.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/database/unicode-considerations.adoc)

Database schema in Keycloak only accounts for Unicode strings in the following special fields:

- Realms: display name, HTML display name
- Federation Providers: display name
- Users: username, given name, last name, attribute names and values
- Groups: name, attribute names and values
- Roles: name
- Descriptions of objects

Otherwise, characters are limited to those contained in database encoding which is often 8-bit. However, for some database systems, it is possible to enable UTF-8 encoding of Unicode characters and use full Unicode character set in all text fields. Often, this is counterbalanced by shorter maximum length of the strings than in case of 8-bit encodings.

Some of the databases require special settings to database and/or JDBC driver to be able to handle Unicode characters. Please find the settings for your database below. Note that if a database is listed here, it can still work properly provided it handles UTF-8 encoding properly both on the level of database and JDBC driver.

Technically, the key criterion for Unicode support for all fields is whether the database allows setting of Unicode character set for `VARCHAR` and `CHAR` fields. If yes, there is a high chance that Unicode will be plausible, usually at the expense of field length. If it only supports Unicode in `NVARCHAR` and `NCHAR` fields, Unicode support for all text fields is unlikely as Keycloak schema uses `VARCHAR` and `CHAR` fields extensively.

#### 6.6.1. Oracle Database

Unicode characters are properly handled provided the database was created with Unicode support in `VARCHAR` and `CHAR`fields (e.g. by using `AL32UTF8` character set as the database character set). No special settings is needed for JDBC driver.

If the database character set is not Unicode, then to use Unicode characters in the special fields, the JDBC driver needs to be configured with the connection property `oracle.jdbc.defaultNChar` set to `true`. It might be wise, though not strictly necessary, to also set the `oracle.jdbc.convertNcharLiterals` connection property to `true`. These properties can be set either as system properties or as connection properties. Please note that setting `oracle.jdbc.defaultNChar` may have negative impact on performance. For details, please refer to Oracle JDBC driver configuration documentation.

#### 6.6.2. Microsoft SQL Server Database

Unicode characters are properly handled only for the special fields. No special settings of JDBC driver or database is necessary.

#### 6.6.3. MySQL Database

Unicode characters are properly handled provided the database was created with Unicode support in `VARCHAR` and `CHAR`fields in the `CREATE DATABASE` command (e.g. by using `utf8` character set as the default database character set in MySQL 5.5. Please note that `utf8mb4` character set does not work due to different storage requirements to `utf8` character set [[1](https://www.keycloak.org/docs/latest/server_installation/index.html#_footnote_1)]). Note that in this case, length restriction to non-special fields does not apply because columns are created to accommodate given amount of characters, not bytes. If the database default character set does not allow storing Unicode, only the special fields allow storing Unicode values.

At the side of JDBC driver settings, it is necessary to add a connection property `characterEncoding=UTF-8` to the JDBC connection settings.

#### 6.6.4. PostgreSQL Database

Unicode is supported when the database character set is `UTF8`. In that case, Unicode characters can be used in any field, there is no reduction of field length for non-special fields. No special settings of JDBC driver is necessary.

## 7. Network Setup

Keycloak can run out of the box with some networking limitations. For one, all network endpoints bind to `localhost` so the auth server is really only usable on one local machine. For HTTP based connections, it does not use default ports like 80 and 443. HTTPS/SSL is not configured out of the box and without it, Keycloak has many security vulnerabilities. Finally, Keycloak may often need to make secure SSL and HTTPS connections to external servers and thus need a trust store set up so that endpoints can be validated correctly. This chapter discusses all of these things.

### 7.1. Bind Addresses

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/network/bind-address.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/network/bind-address.adoc)

By default Keycloak binds to the localhost loopback address `127.0.0.1`. That’s not a very useful default if you want the authentication server available on your network. Generally, what we recommend is that you deploy a reverse proxy or load balancer on a public network and route traffic to individual Keycloak server instances on a private network. In either case though, you still need to set up your network interfaces to bind to something other than `localhost`.

Setting the bind address is quite easy and can be done on the command line with either the *standalone.sh* or *domain.sh* boot scripts discussed in the [Choosing an Operating Mode](https://www.keycloak.org/docs/latest/server_installation/index.html#_operating-mode) chapter.

```
$ standalone.sh -b 192.168.0.5
```

The `-b` switch sets the IP bind address for any public interfaces.

Alternatively, if you don’t want to set the bind address at the command line, you can edit the profile configuration of your deployment. Open up the profile configuration file (*standalone.xml* or *domain.xml* depending on your [operating mode](https://www.keycloak.org/docs/latest/server_installation/index.html#_operating-mode)) and look for the `interfaces` XML block.

```
    <interfaces>
        <interface name="management">
            <inet-address value="${jboss.bind.address.management:127.0.0.1}"/>
        </interface>
        <interface name="public">
            <inet-address value="${jboss.bind.address:127.0.0.1}"/>
        </interface>
    </interfaces>
```

The `public` interface corresponds to subsystems creating sockets that are available publicly. An example of one of these subsystems is the web layer which serves up the authentication endpoints of Keycloak. The `management` interface corresponds to sockets opened up by the management layer of the WildFly. Specifically the sockets which allow you to use the `jboss-cli.sh` command line interface and the WildFly web console.

In looking at the `public` interface you see that it has a special string `${jboss.bind.address:127.0.0.1}`. This string denotes a value `127.0.0.1` that can be overridden on the command line by setting a Java system property, i.e.:

```
$ domain.sh -Djboss.bind.address=192.168.0.5
```

The `-b` is just a shorthand notation for this command. So, you can either change the bind address value directly in the profile config, or change it on the command line when you boot up.

|      | There are many more options available when setting up `interface` definitions. For more information, see [the network interface](http://docs.wildfly.org/16/Admin_Guide.html#Interfaces_and_ports) in the *WildFly 16 Documentation*. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 7.2. Socket Port Bindings

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/network/ports.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/network/ports.adoc)

The ports opened for each socket have a pre-defined default that can be overridden at the command line or within configuration. To illustrate this configuration, let’s pretend you are running in [standalone mode](https://www.keycloak.org/docs/latest/server_installation/index.html#_standalone-mode) and open up the *…/standalone/configuration/standalone.xml*. Search for `socket-binding-group`.

```
    <socket-binding-group name="standard-sockets" default-interface="public" port-offset="${jboss.socket.binding.port-offset:0}">
        <socket-binding name="management-http" interface="management" port="${jboss.management.http.port:9990}"/>
        <socket-binding name="management-https" interface="management" port="${jboss.management.https.port:9993}"/>
        <socket-binding name="ajp" port="${jboss.ajp.port:8009}"/>
        <socket-binding name="http" port="${jboss.http.port:8080}"/>
        <socket-binding name="https" port="${jboss.https.port:8443}"/>
        <socket-binding name="txn-recovery-environment" port="4712"/>
        <socket-binding name="txn-status-manager" port="4713"/>
        <outbound-socket-binding name="mail-smtp">
            <remote-destination host="localhost" port="25"/>
        </outbound-socket-binding>
    </socket-binding-group>
```

`socket-bindings` define socket connections that will be opened by the server. These bindings specify the `interface`(bind address) they use as well as what port number they will open. The ones you will be most interested in are:

- http

  Defines the port used for Keycloak HTTP connections

- https

  Defines the port used for Keycloak HTTPS connections

- ajp

  This socket binding defines the port used for the AJP protocol. This protocol is used by Apache HTTPD server in conjunction `mod-cluster` when you are using Apache HTTPD as a load balancer.

- management-http

  Defines the HTTP connection used by WildFly CLI and web console.

When running in [domain mode](https://www.keycloak.org/docs/latest/server_installation/index.html#_domain-mode) setting the socket configurations is a bit trickier as the example *domain.xml* file has multiple `socket-binding-groups` defined. If you scroll down to the `server-group` definitions you can see what `socket-binding-group` is used for each `server-group`.

domain socket bindings

```
    <server-groups>
        <server-group name="load-balancer-group" profile="load-balancer">
            ...
            <socket-binding-group ref="load-balancer-sockets"/>
        </server-group>
        <server-group name="auth-server-group" profile="auth-server-clustered">
            ...
            <socket-binding-group ref="ha-sockets"/>
        </server-group>
    </server-groups>
```

|      | There are many more options available when setting up `socket-binding-group` definitions. For more information, see [the socket binding group](http://docs.wildfly.org/16/Admin_Guide.html#Interfaces_and_ports) in the *WildFly 16 Documentation*. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 7.3. Setting up HTTPS/SSL

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/network/https.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/network/https.adoc)

|      | Keycloak is not set up by default to handle SSL/HTTPS. It is highly recommended that you either enable SSL on the Keycloak server itself or on a reverse proxy in front of the Keycloak server. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

This default behavior is defined by the SSL/HTTPS mode of each Keycloak realm. This is discussed in more detail in the [Server Administration Guide](https://www.keycloak.org/docs/6.0/server_admin/), but let’s give some context and a brief overview of these modes.

- external requests

  Keycloak can run out of the box without SSL so long as you stick to private IP addresses like `localhost`, `127.0.0.1`, `10.0.x.x`, `192.168.x.x`, and `172.16.x.x`. If you don???t have SSL/HTTPS configured on the server or you try to access Keycloak over HTTP from a non-private IP adress you will get an error.

- none

  Keycloak does not require SSL. This should really only be used in development when you are playing around with things.

- all requests

  Keycloak requires SSL for all IP addresses.

The SSL mode for each realm can be configured in the Keycloak admin console.

#### 7.3.1. Enabling SSL/HTTPS for the Keycloak Server

If you are not using a reverse proxy or load balancer to handle HTTPS traffic for you, you’ll need to enable HTTPS for the Keycloak server. This involves

1. Obtaining or generating a keystore that contains the private key and certificate for SSL/HTTP traffic
2. Configuring the Keycloak server to use this keypair and certificate.

##### Creating the Certificate and Java Keystore

In order to allow HTTPS connections, you need to obtain a self signed or third-party signed certificate and import it into a Java keystore before you can enable HTTPS in the web container you are deploying the Keycloak Server to.

###### Self Signed Certificate

In development, you will probably not have a third party signed certificate available to test a Keycloak deployment so you’ll need to generate a self-signed one using the `keytool` utility that comes with the Java JDK.

```
$ keytool -genkey -alias localhost -keyalg RSA -keystore keycloak.jks -validity 10950
    Enter keystore password: secret
    Re-enter new password: secret
    What is your first and last name?
    [Unknown]:  localhost
    What is the name of your organizational unit?
    [Unknown]:  Keycloak
    What is the name of your organization?
    [Unknown]:  Red Hat
    What is the name of your City or Locality?
    [Unknown]:  Westford
    What is the name of your State or Province?
    [Unknown]:  MA
    What is the two-letter country code for this unit?
    [Unknown]:  US
    Is CN=localhost, OU=Keycloak, O=Test, L=Westford, ST=MA, C=US correct?
    [no]:  yes
```

You should answer `What is your first and last name ?` question with the DNS name of the machine you’re installing the server on. For testing purposes, `localhost` should be used. After executing this command, the `keycloak.jks` file will be generated in the same directory as you executed the `keytool` command in.

If you want a third-party signed certificate, but don’t have one, you can obtain one for free at [cacert.org](http://www.cacert.org/). You’ll have to do a little set up first before doing this though.

The first thing to do is generate a Certificate Request:

```
$ keytool -certreq -alias yourdomain -keystore keycloak.jks > keycloak.careq
```

Where `yourdomain` is a DNS name for which this certificate is generated for. Keytool generates the request:

```
-----BEGIN NEW CERTIFICATE REQUEST-----
MIIC2jCCAcICAQAwZTELMAkGA1UEBhMCVVMxCzAJBgNVBAgTAk1BMREwDwYDVQQHEwhXZXN0Zm9y
ZDEQMA4GA1UEChMHUmVkIEhhdDEQMA4GA1UECxMHUmVkIEhhdDESMBAGA1UEAxMJbG9jYWxob3N0
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr7kck2TaavlEOGbcpi9c0rncY4HhdzmY
Ax2nZfq1eZEaIPqI5aTxwQZzzLDK9qbeAd8Ji79HzSqnRDxNYaZu7mAYhFKHgixsolE3o5Yfzbw1
29RvyeUVe+WZxv5oo9wolVVpdSINIMEL2LaFhtX/c1dqiqYVpfnvFshZQaIg2nL8juzZcBjj4as
H98gIS7khql/dkZKsw9NLvyxgJvp7PaXurX29fNf3ihG+oFrL22oFyV54BWWxXCKU/GPn61EGZGw
Ft2qSIGLdctpMD1aJR2bcnlhEjZKDksjQZoQ5YMXaAGkcYkG6QkgrocDE2YXDbi7GIdf9MegVJ35
2DQMpwIDAQABoDAwLgYJKoZIhvcNAQkOMSEwHzAdBgNVHQ4EFgQUQwlZJBA+fjiDdiVzaO9vrE/i
n2swDQYJKoZIhvcNAQELBQADggEBAC5FRvMkhal3q86tHPBYWBuTtmcSjs4qUm6V6f63frhveWHf
PzRrI1xH272XUIeBk0gtzWo0nNZnf0mMCtUBbHhhDcG82xolikfqibZijoQZCiGiedVjHJFtniDQ
9bMDUOXEMQ7gHZg5q6mJfNG9MbMpQaUVEEFvfGEQQxbiFK7hRWU8S23/d80e8nExgQxdJWJ6vd0X
MzzFK6j4Dj55bJVuM7GFmfdNC52pNOD5vYe47Aqh8oajHX9XTycVtPXl45rrWAH33ftbrS8SrZ2S
vqIFQeuLL3BaHwpl3t7j2lMWcK1p80laAxEASib/fAwrRHpLHBXRcq6uALUOZl4Alt8=
-----END NEW CERTIFICATE REQUEST-----
```

Send this ca request to your CA. The CA will issue you a signed certificate and send it to you. Before you import your new cert, you must obtain and import the root certificate of the CA. You can download the cert from CA (ie.: root.crt) and import as follows:

```
$ keytool -import -keystore keycloak.jks -file root.crt -alias root
```

Last step is to import your new CA generated certificate to your keystore:

```
$ keytool -import -alias yourdomain -keystore keycloak.jks -file your-certificate.cer
```

##### Configure Keycloak to Use the Keystore

Now that you have a Java keystore with the appropriate certificates, you need to configure your Keycloak installation to use it. First, you must edit the *standalone.xml*, *standalone-ha.xml*, or *host.xml* file to use the keystore and enable HTTPS. You may then either move the keystore file to the *configuration/* directory of your deployment or the file in a location you choose and provide an absolute path to it. If you are using absolute paths, remove the optional `relative-to` parameter from your configuration (See [operating mode](https://www.keycloak.org/docs/latest/server_installation/index.html#_operating-mode)).

Add the new `security-realm` element using the CLI:

```
$ /core-service=management/security-realm=UndertowRealm:add()

$ /core-service=management/security-realm=UndertowRealm/server-identity=ssl:add(keystore-path=keycloak.jks, keystore-relative-to=jboss.server.config.dir, keystore-password=secret)
```

If using domain mode, the commands should be executed in every host using the `/host=<host_name>/` prefix (in order to create the `security-realm` in all of them), like this, which you would repeat for each host:

```
$ /host=<host_name>/core-service=management/security-realm=UndertowRealm/server-identity=ssl:add(keystore-path=keycloak.jks, keystore-relative-to=jboss.server.config.dir, keystore-password=secret)
```

In the standalone or host configuration file, the `security-realms` element should look like this:

```
<security-realm name="UndertowRealm">
    <server-identities>
        <ssl>
            <keystore path="keycloak.jks" relative-to="jboss.server.config.dir" keystore-password="secret" />
        </ssl>
    </server-identities>
</security-realm>
```

Next, in the standalone or each domain configuration file, search for any instances of `security-realm`. Modify the `https-listener` to use the created realm:

```
$ /subsystem=undertow/server=default-server/https-listener=https:write-attribute(name=security-realm, value=UndertowRealm)
```

If using domain mode, prefix the command with the profile that is being used with: `/profile=<profile_name>/`.

The resulting element, `server name="default-server"`, which is a child element of `subsystem xmlns="urn:jboss:domain:undertow:8.0"`, should contain the following stanza:

```
<subsystem xmlns="urn:jboss:domain:undertow:8.0">
   <buffer-cache name="default"/>
   <server name="default-server">
      <https-listener name="https" socket-binding="https" security-realm="UndertowRealm"/>
   ...
</subsystem>
```

### 7.4. Outgoing HTTP Requests

The Keycloak server often needs to make non-browser HTTP requests to the applications and services it secures. The auth server manages these outgoing connections by maintaining an HTTP client connection pool. There are some things you’ll need to configure in `standalone.xml`, `standalone-ha.xml`, or `domain.xml`. The location of this file depends on your [operating mode](https://www.keycloak.org/docs/latest/server_installation/index.html#_operating-mode).

HTTP client Config example

```
<spi name="connectionsHttpClient">
    <provider name="default" enabled="true">
        <properties>
            <property name="connection-pool-size" value="256"/>
        </properties>
    </provider>
</spi>
```

Possible configuration options are:

- establish-connection-timeout-millis

  Timeout for establishing a socket connection.

- socket-timeout-millis

  If an outgoing request does not receive data for this amount of time, timeout the connection.

- connection-pool-size

  How many connections can be in the pool (128 by default).

- max-pooled-per-route

  How many connections can be pooled per host (64 by default).

- connection-ttl-millis

  Maximum connection time to live in milliseconds. Not set by default.

- max-connection-idle-time-millis

  Maximum time the connection might stay idle in the connection pool (900 seconds by default). Will start background cleaner thread of Apache HTTP client. Set to -`1` to disable this checking and the background thread.

- disable-cookies

  `true` by default. When set to true, this will disable any cookie caching.

- client-keystore

  This is the file path to a Java keystore file. This keystore contains client certificate for two-way SSL.

- client-keystore-password

  Password for the client keystore. This is *REQUIRED* if `client-keystore` is set.

- client-key-password

  Password for the client’s key. This is *REQUIRED* if `client-keystore` is set.

- proxy-mappings

  Dennotes proxy configurations for outgoing HTTP requests. See the section on [Proxy Mappings for Outgoing HTTP Requests](https://www.keycloak.org/docs/latest/server_installation/index.html#_proxymappings) for more details.

#### 7.4.1. Proxy Mappings for Outgoing HTTP Requests

Outgoing HTTP requests sent by Keycloak can optionally use a proxy server based on a comma delimited list of proxy-mappings. A proxy-mapping denotes the combination of a regex based hostname pattern and a proxy-uri in the form of `hostnamePattern;proxyUri`, e.g.:

```
.*\.(google|googleapis)\.com;http://www-proxy.acme.com:8080
```

To determine the proxy for an outgoing HTTP request the target hostname is matched against the configured hostname patterns. The first matching pattern determines the proxy-uri to use. If none of the configured patterns match for the given hostname then no proxy is used.

The special value `NO_PROXY` for the proxy-uri can be used to indicate that no proxy should be used for hosts matching the associated hostname pattern. It is possible to specify a catch-all pattern at the end of the proxy-mappings to define a default proxy for all outgoing requests.

The following example demonstrates the proxy-mapping configuration.

```
# All requests to Google APIs should use http://www-proxy.acme.com:8080 as proxy
.*\.(google|googleapis)\.com;http://www-proxy.acme.com:8080

# All requests to internal systems should use no proxy
.*\.acme\.com;NO_PROXY

# All other requests should use http://fallback:8080 as proxy
.*;http://fallback:8080
```

This can be configured via the following `jboss-cli` command. Note that you need to properly escape the regex-pattern as shown below.

```
echo SETUP: Configure proxy routes for HttpClient SPI

# In case there is no connectionsHttpClient definition yet
/subsystem=keycloak-server/spi=connectionsHttpClient/provider=default:add(enabled=true)

# Configure the proxy-mappings
/subsystem=keycloak-server/spi=connectionsHttpClient/provider=default:write-attribute(name=properties.proxy-mappings,value=[".*\\.(google|googleapis)\\.com;http://www-proxy.acme.com:8080",".*\\.acme\\.com;NO_PROXY",".*;http://fallback:8080"])
```

The `jboss-cli` command results in the following subsystem configuration. Note that one needs to encode `"` characters with `"`.

```
<spi name="connectionsHttpClient">
    <provider name="default" enabled="true">
        <properties>
            <property
            name="proxy-mappings"
            value="[&quot;.*\\.(google|googleapis)\\.com;http://www-proxy.acme.com:8080&quot;,&quot;.*\\.acme\\.com;NO_PROXY&quot;,&quot;.*;http://fallback:8080&quot;]"/>
        </properties>
    </provider>
</spi>
```

#### 7.4.2. Outgoing HTTPS Request Truststore

When Keycloak invokes on remote HTTPS endpoints, it has to validate the remote server’s certificate in order to ensure it is connecting to a trusted server. This is necessary in order to prevent man-in-the-middle attacks. The certificates of these remote server’s or the CA that signed these certificates must be put in a truststore. This truststore is managed by the Keycloak server.

The truststore is used when connecting securely to identity brokers, LDAP identity providers, when sending emails, and for backchannel communication with client applications.

|      | By default, a truststore provider is not configured, and any https connections fall back to standard java truststore configuration as described in [Java’s JSSE Reference Guide](https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html). If there is no trust established, then these outgoing HTTPS requests will fail. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

You can use *keytool* to create a new truststore file or add trusted host certificates to an existing one:

```
$ keytool -import -alias HOSTDOMAIN -keystore truststore.jks -file host-certificate.cer
```

The truststore is configured within the `standalone.xml`, `standalone-ha.xml`, or `domain.xml` file in your distribution. The location of this file depends on your [operating mode](https://www.keycloak.org/docs/latest/server_installation/index.html#_operating-mode). You can add your truststore configuration by using the following template:

```
<spi name="truststore">
    <provider name="file" enabled="true">
        <properties>
            <property name="file" value="path to your .jks file containing public certificates"/>
            <property name="password" value="password"/>
            <property name="hostname-verification-policy" value="WILDCARD"/>
            <property name="disabled" value="false"/>
        </properties>
    </provider>
</spi>
```

Possible configuration options for this setting are:

- file

  The path to a Java keystore file. HTTPS requests need a way to verify the host of the server they are talking to. This is what the trustore does. The keystore contains one or more trusted host certificates or certificate authorities. This truststore file should only contain public certificates of your secured hosts. This is *REQUIRED* if `disabled` is not true.

- password

  Password for the truststore. This is *REQUIRED* if `disabled` is not true.

- hostname-verification-policy

  `WILDCARD` by default. For HTTPS requests, this verifies the hostname of the server’s certificate. `ANY` means that the hostname is not verified. `WILDCARD` Allows wildcards in subdomain names i.e. *.foo.com. `STRICT` CN must match hostname exactly.

- disabled

  If true (default value), truststore configuration will be ignored, and certificate checking will fall back to JSSE configuration as described. If set to false, you must configure `file`, and `password` for the truststore.

## 8. Clustering


  This section covers configuring Keycloak to run in a cluster. There’s a number of things you have to do when setting up a cluster, specifically:

- [Pick an operation mode](https://www.keycloak.org/docs/latest/server_installation/index.html#_operating-mode)
- [Configure a shared external database](https://www.keycloak.org/docs/latest/server_installation/index.html#_database)
- Set up a load balancer
- Supplying a private network that supports IP multicast

Picking an operation mode and configuring a shared database have been discussed earlier in this guide. In this chapter we’ll discuss setting up a load balancer and supplying a private network. We’ll also discuss some issues that you need to be aware of when booting up a host in the cluster.

|      | It is possible to cluster Keycloak without IP Multicast, but this topic is beyond the scope of this guide. For more information, see [JGroups](http://docs.wildfly.org/16/High_Availability_Guide.html#JGroups_Subsystem) chapter of the *WildFly 16 Documentation*. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 8.1. Recommended Network Architecture

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/clustering/recommended.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/clustering/recommended.adoc)

The recommended network architecture for deploying Keycloak is to set up an HTTP/HTTPS load balancer on a public IP address that routes requests to Keycloak servers sitting on a private network. This isolates all clustering connections and provides a nice means of protecting the servers.

|      | By default, there is nothing to prevent unauthorized nodes from joining the cluster and broadcasting multicast messages. This is why cluster nodes should be in a private network, with a firewall protecting them from outside attacks. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 8.2. Clustering Example

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/clustering/example.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/clustering/example.adoc)

Keycloak does come with an out of the box clustering demo that leverages domain mode. Review the[Clustered Domain Example](https://www.keycloak.org/docs/latest/server_installation/index.html#_clustered-domain-example) chapter for more details.

### 8.3. Setting Up a Load Balancer or Proxy

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/clustering/load-balancer.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/clustering/load-balancer.adoc)

This section discusses a number of things you need to configure before you can put a reverse proxy or load balancer in front of your clustered Keycloak deployment. It also covers configuring the built in load balancer that was [Clustered Domain Example](https://www.keycloak.org/docs/latest/server_installation/index.html#_clustered-domain-example).

#### 8.3.1. Identifying Client IP Addresses

A few features in Keycloak rely on the fact that the remote address of the HTTP client connecting to the authentication server is the real IP address of the client machine. Examples include:

- Event logs - a failed login attempt would be logged with the wrong source IP address
- SSL required - if the SSL required is set to external (the default) it should require SSL for all external requests
- Authentication flows - a custom authentication flow that uses the IP address to for example show OTP only for external requests
- Dynamic Client Registration

This can be problematic when you have a reverse proxy or loadbalancer in front of your Keycloak authentication server. The usual setup is that you have a frontend proxy sitting on a public network that load balances and forwards requests to backend Keycloak server instances located in a private network. There is some extra configuration you have to do in this scenario so that the actual client IP address is forwarded to and processed by the Keycloak server instances. Specifically:

- Configure your reverse proxy or loadbalancer to properly set `X-Forwarded-For` and `X-Forwarded-Proto` HTTP headers.
- Configure your reverse proxy or loadbalancer to preserve the original 'Host' HTTP header.
- Configure the authentication server to read the client’s IP address from `X-Forwarded-For` header.

Configuring your proxy to generate the `X-Forwarded-For` and `X-Forwarded-Proto` HTTP headers and preserving the original `Host` HTTP header is beyond the scope of this guide. Take extra precautions to ensure that the `X-Forwared-For`header is set by your proxy. If your proxy isn’t configured correctly, then *rogue* clients can set this header themselves and trick Keycloak into thinking the client is connecting from a different IP address than it actually is. This becomes really important if you are doing any black or white listing of IP addresses.

Beyond the proxy itself, there are a few things you need to configure on the Keycloak side of things. If your proxy is forwarding requests via the HTTP protocol, then you need to configure Keycloak to pull the client’s IP address from the `X-Forwarded-For` header rather than from the network packet. To do this, open up the profile configuration file (*standalone.xml*, *standalone-ha.xml*, or *domain.xml* depending on your [operating mode](https://www.keycloak.org/docs/latest/server_installation/index.html#_operating-mode)) and look for the `urn:jboss:domain:undertow:8.0` XML block.

`X-Forwarded-For` HTTP Config

```
<subsystem xmlns="urn:jboss:domain:undertow:8.0">
   <buffer-cache name="default"/>
   <server name="default-server">
      <ajp-listener name="ajp" socket-binding="ajp"/>
      <http-listener name="default" socket-binding="http" redirect-socket="https"
          proxy-address-forwarding="true"/>
      ...
   </server>
   ...
</subsystem>
```

Add the `proxy-address-forwarding` attribute to the `http-listener` element. Set the value to `true`.

If your proxy is using the AJP protocol instead of HTTP to forward requests (i.e. Apache HTTPD + mod-cluster), then you have to configure things a little differently. Instead of modifying the `http-listener`, you need to add a filter to pull this information from the AJP packets.

`X-Forwarded-For` AJP Config

```
<subsystem xmlns="urn:jboss:domain:undertow:8.0">
     <buffer-cache name="default"/>
     <server name="default-server">
         <ajp-listener name="ajp" socket-binding="ajp"/>
         <http-listener name="default" socket-binding="http" redirect-socket="https"/>
         <host name="default-host" alias="localhost">
             ...
             <filter-ref name="proxy-peer"/>
         </host>
     </server>
        ...
     <filters>
         ...
         <filter name="proxy-peer"
                 class-name="io.undertow.server.handlers.ProxyPeerAddressHandler"
                 module="io.undertow.core" />
     </filters>
 </subsystem>
```

#### 8.3.2. Enable HTTPS/SSL with a Reverse Proxy

Assuming that your reverse proxy doesn’t use port 8443 for SSL you also need to configure what port HTTPS traffic is redirected to.

```
<subsystem xmlns="urn:jboss:domain:undertow:8.0">
    ...
    <http-listener name="default" socket-binding="http"
        proxy-address-forwarding="true" redirect-socket="proxy-https"/>
    ...
</subsystem>
```

Add the `redirect-socket` attribute to the `http-listener` element. The value should be `proxy-https` which points to a socket binding you also need to define.

Then add a new `socket-binding` element to the `socket-binding-group` element:

```
<socket-binding-group name="standard-sockets" default-interface="public"
    port-offset="${jboss.socket.binding.port-offset:0}">
    ...
    <socket-binding name="proxy-https" port="443"/>
    ...
</socket-binding-group>
```

#### 8.3.3. Verify Configuration

You can verify the reverse proxy or load balancer configuration by opening the path `/auth/realms/master/.well-known/openid-configuration` through the reverse proxy. For example if the reverse proxy address is `https://acme.com/` then open the URL `https://acme.com/auth/realms/master/.well-known/openid-configuration`. This will show a JSON document listing a number of endpoints for Keycloak. Make sure the endpoints starts with the address (scheme, domain and port) of your reverse proxy or load balancer. By doing this you make sure that Keycloak is using the correct endpoint.

You should also verify that Keycloak sees the correct source IP address for requests. Do check this you can try to login to the admin console with an invalid username and/or password. This should show a warning in the server log something like this:

```
08:14:21,287 WARN  XNIO-1 task-45 [org.keycloak.events] type=LOGIN_ERROR, realmId=master, clientId=security-admin-console, userId=8f20d7ba-4974-4811-a695-242c8fbd1bf8, ipAddress=X.X.X.X, error=invalid_user_credentials, auth_method=openid-connect, auth_type=code, redirect_uri=http://localhost:8080/auth/admin/master/console/?redirect_fragment=%2Frealms%2Fmaster%2Fevents-settings, code_id=a3d48b67-a439-4546-b992-e93311d6493e, username=admin
```

Check that the value of `ipAddress` is the IP address of the machine you tried to login with and not the IP address of the reverse proxy or load balancer.

#### 8.3.4. Using the Built-In Load Balancer

This section covers configuring the built in load balancer that is discussed in the [Clustered Domain Example](https://www.keycloak.org/docs/latest/server_installation/index.html#_clustered-domain-example).

The [Clustered Domain Example](https://www.keycloak.org/docs/latest/server_installation/index.html#_clustered-domain-example) is only designed to run on one machine. To bring up a slave on another host, you’ll need to

1. Edit the *domain.xml* file to point to your new host slave
2. Copy the server distribution. You don’t need the *domain.xml*, *host.xml*, or *host-master.xml* files. Nor do you need the *standalone/* directory.
3. Edit the *host-slave.xml* file to change the bind addresses used or override them on the command line

##### Register a New Host With Load Balancer

Let’s look first at registering the new host slave with the load balancer configuration in *domain.xml*. Open this file and go to the undertow configuration in the `load-balancer` profile. Add a new `host` definition called `remote-host3` within the `reverse-proxy` XML block.

domain.xml reverse-proxy config

```
<subsystem xmlns="urn:jboss:domain:undertow:8.0">
  ...
  <handlers>
      <reverse-proxy name="lb-handler">
         <host name="host1" outbound-socket-binding="remote-host1" scheme="ajp" path="/" instance-id="myroute1"/>
         <host name="host2" outbound-socket-binding="remote-host2" scheme="ajp" path="/" instance-id="myroute2"/>
         <host name="remote-host3" outbound-socket-binding="remote-host3" scheme="ajp" path="/" instance-id="myroute3"/>
      </reverse-proxy>
  </handlers>
  ...
</subsystem>
```

The `output-socket-binding` is a logical name pointing to a `socket-binding` configured later in the *domain.xml* file. the `instance-id` attribute must also be unique to the new host as this value is used by a cookie to enable sticky sessions when load balancing.

Next go down to the `load-balancer-sockets` `socket-binding-group` and add the `outbound-socket-binding` for `remote-host3`. This new binding needs to point to the host and port of the new host.

domain.xml outbound-socket-binding

```
<socket-binding-group name="load-balancer-sockets" default-interface="public">
    ...
    <outbound-socket-binding name="remote-host1">
        <remote-destination host="localhost" port="8159"/>
    </outbound-socket-binding>
    <outbound-socket-binding name="remote-host2">
        <remote-destination host="localhost" port="8259"/>
    </outbound-socket-binding>
    <outbound-socket-binding name="remote-host3">
        <remote-destination host="192.168.0.5" port="8259"/>
    </outbound-socket-binding>
</socket-binding-group>
```

##### Master Bind Addresses

Next thing you’ll have to do is to change the `public` and `management` bind addresses for the master host. Either edit the *domain.xml* file as discussed in the [Bind Addresses](https://www.keycloak.org/docs/latest/server_installation/index.html#_bind-address) chapter or specify these bind addresses on the command line as follows:

```
$ domain.sh --host-config=host-master.xml -Djboss.bind.address=192.168.0.2 -Djboss.bind.address.management=192.168.0.2
```

##### Host Slave Bind Addresses

Next you’ll have to change the `public`, `management`, and domain controller bind addresses (`jboss.domain.master-address`). Either edit the *host-slave.xml* file or specify them on the command line as follows:

```
$ domain.sh --host-config=host-slave.xml
     -Djboss.bind.address=192.168.0.5
      -Djboss.bind.address.management=192.168.0.5
       -Djboss.domain.master.address=192.168.0.2
```

The values of `jboss.bind.address` and `jboss.bind.addres.management` pertain to the host slave’s IP address. The value of `jboss.domain.master.address` need to be the IP address of the domain controller which is the management address of the master host.

#### 8.3.5. Configuring Other Load Balancers

See [the load balancing](http://docs.wildfly.org/16/High_Availability_Guide.html) section in the *WildFly 16 Documentation* for information how to use other software-based load balancers.

### 8.4. Sticky sessions

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/clustering/sticky-sessions.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/clustering/sticky-sessions.adoc)

Typical cluster deployment consists of the load balancer (reverse proxy) and 2 or more Keycloak servers on private network. For performance purposes, it may be useful if load balancer forwards all requests related to particular browser session to the same Keycloak backend node.

The reason is, that Keycloak is using Infinispan distributed cache under the covers for save data related to current authentication session and user session. The Infinispan distributed caches are configured with one owner by default. That means that particular session is saved just on one cluster node and the other nodes need to lookup the session remotely if they want to access it.

For example if authentication session with ID `123` is saved in the Infinispan cache on `node1`, and then `node2` needs to lookup this session, it needs to send the request to `node1` over the network to return the particular session entity.

It is beneficial if particular session entity is always available locally, which can be done with the help of sticky sessions. The workflow in the cluster environment with the public frontend load balancer and two backend Keycloak nodes can be like this:

- User sends initial request to see the Keycloak login screen
- This request is served by the frontend load balancer, which forwards it to some random node (eg. node1). Strictly said, the node doesn’t need to be random, but can be chosen according to some other criterias (client IP address etc). It all depends on the implementation and configuration of underlying load balancer (reverse proxy).
- Keycloak creates authentication session with random ID (eg. 123) and saves it to the Infinispan cache.
- Infinispan distributed cache assigns the primary owner of the session based on the hash of session ID. See [Infinispan documentation](http://infinispan.org/docs/8.2.x/user_guide/user_guide.html#distribution_mode) for more details around this. Let’s assume that Infinispan assigned `node2` to be the owner of this session.
- Keycloak creates the cookie `AUTH_SESSION_ID` with the format like `<session-id>.<owner-node-id>` . In our example case, it will be `123.node2` .
- Response is returned to the user with the Keycloak login screen and the AUTH_SESSION_ID cookie in the browser

From this point, it is beneficial if load balancer forwards all the next requests to the `node2` as this is the node, who is owner of the authentication session with ID `123` and hence Infinispan can lookup this session locally. After authentication is finished, the authentication session is converted to user session, which will be also saved on `node2` because it has same ID `123` .

The sticky session is not mandatory for the cluster setup, however it is good for performance for the reasons mentioned above. You need to configure your loadbalancer to sticky over the `AUTH_SESSION_ID` cookie. How exactly do this is dependent on your loadbalancer.

It is recommended on the Keycloak side to use the system property `jboss.node.name` during startup, with the value corresponding to the name of your route. For example, `-Djboss.node.name=node1` will use `node1` to identify the route. This route will be used by Infinispan caches and will be attached to the AUTH_SESSION_ID cookie when the node is the owner of the particular key. Here is an example of the start up command using this system property:

```
cd $RHSSO_NODE1
./standalone.sh -c standalone-ha.xml -Djboss.socket.binding.port-offset=100 -Djboss.node.name=node1
```

Typically in production environment the route name should use the same name as your backend host, but it is not required. You can use a different route name. For example, if you want to hide the host name of your Keycloak server inside your private network.

#### 8.4.1. Disable adding the route

Some load balancers can be configured to add the route information by themselves instead of relying on the back end Keycloak node. However, as described above, adding the route by the Keycloak is recommended. This is because when done this way performance improves, since Keycloak is aware of the entity that is the owner of particular session and can route to that node, which is not necessarily the local node.

You are permitted to disable adding route information to the AUTH_SESSION_ID cookie by Keycloak, if you prefer, by adding the following into your `RHSSO_HOME/standalone/configuration/standalone-ha.xml` file in the Keycloak subsystem configuration:

```
<subsystem xmlns="urn:jboss:domain:keycloak-server:1.1">
  ...
    <spi name="stickySessionEncoder">
        <provider name="infinispan" enabled="true">
            <properties>
                <property name="shouldAttachRoute" value="false"/>
            </properties>
        </provider>
    </spi>

</subsystem>
```

### 8.5. Multicast Network Setup

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/clustering/multicast.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/clustering/multicast.adoc)

Out of the box clustering support needs IP Multicast. Multicast is a network broadcast protocol. This protocol is used at boot time to discover and join the cluster. It is also used to broadcast messages for the replication and invalidation of distributed caches used by Keycloak.

The clustering subsystem for Keycloak runs on the JGroups stack. Out of the box, the bind addresses for clustering are bound to a private network interface with 127.0.0.1 as default IP address. You have to edit your the *standalone-ha.xml* or *domain.xml*sections discussed in the [Bind Address](https://www.keycloak.org/docs/latest/server_installation/index.html#_bind-address) chapter.

private network config

```
    <interfaces>
        ...
        <interface name="private">
            <inet-address value="${jboss.bind.address.private:127.0.0.1}"/>
        </interface>
    </interfaces>
    <socket-binding-group name="standard-sockets" default-interface="public" port-offset="${jboss.socket.binding.port-offset:0}">
        ...
        <socket-binding name="jgroups-mping" interface="private" port="0" multicast-address="${jboss.default.multicast.address:230.0.0.4}" multicast-port="45700"/>
        <socket-binding name="jgroups-tcp" interface="private" port="7600"/>
        <socket-binding name="jgroups-tcp-fd" interface="private" port="57600"/>
        <socket-binding name="jgroups-udp" interface="private" port="55200" multicast-address="${jboss.default.multicast.address:230.0.0.4}" multicast-port="45688"/>
        <socket-binding name="jgroups-udp-fd" interface="private" port="54200"/>
        <socket-binding name="modcluster" port="0" multicast-address="224.0.1.105" multicast-port="23364"/>
        ...
    </socket-binding-group>
```

Things you’ll want to configure are the `jboss.bind.address.private` and `jboss.default.multicast.address` as well as the ports of the services on the clustering stack.

|      | It is possible to cluster Keycloak without IP Multicast, but this topic is beyond the scope of this guide. For more information, see [JGroups](http://docs.wildfly.org/16/High_Availability_Guide.html#JGroups_Subsystem) in the *WildFly 16 Documentation*. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 8.6. Securing Cluster Communication

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/clustering/securing-cluster-comm.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/clustering/securing-cluster-comm.adoc)

When cluster nodes are isolated on a private network it requires access to the private network to be able to join a cluster or to view communication in the cluster. In addition you can also enable authentication and encryption for cluster communication. As long as your private network is secure it is not necessary to enable authentication and encryption. Keycloak does not send very sensitive information on the cluster in either case.

If you want to enable authentication and encryption for clustering communication see [Securing a Cluster](https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html/configuration_guide/configuring_high_availability#securing_cluster) in the *JBoss EAP Configuration Guide*.

### 8.7. Serialized Cluster Startup

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/clustering/serialized.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/clustering/serialized.adoc)

Keycloak cluster nodes are allowed to boot concurrently. When Keycloak server instance boots up it may do some database migration, importing, or first time initializations. A DB lock is used to prevent start actions from conflicting with one another when cluster nodes boot up concurrently.

By default, the maximum timeout for this lock is 900 seconds. If a node is waiting on this lock for more than the timeout it will fail to boot. Typically you won’t need to increase/decrease the default value, but just in case it’s possible to configure it in`standalone.xml`, `standalone-ha.xml`, or `domain.xml` file in your distribution. The location of this file depends on your [operating mode](https://www.keycloak.org/docs/latest/server_installation/index.html#_operating-mode).

```
<spi name="dblock">
    <provider name="jpa" enabled="true">
        <properties>
            <property name="lockWaitTimeout" value="900"/>
        </properties>
    </provider>
</spi>
```

### 8.8. Booting the Cluster

Booting Keycloak in a cluster depends on your [operating mode](https://www.keycloak.org/docs/latest/server_installation/index.html#_operating-mode)

Standalone Mode

```
$ bin/standalone.sh --server-config=standalone-ha.xml
```

Domain Mode

```
$ bin/domain.sh --host-config=host-master.xml
$ bin/domain.sh --host-config=host-slave.xml
```

You may need to use additional parameters or system properties. For example, the parameter `-b` for the binding host or the system property `jboss.node.name` to specify the name of the route, as described in [Sticky Sessions](https://www.keycloak.org/docs/latest/server_installation/index.html#sticky-sessions) section.

### 8.9. Troubleshooting

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/clustering/troubleshooting.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/clustering/troubleshooting.adoc)

- Note that when you run a cluster, you should see message similar to this in the log of both cluster nodes:

  ```
  INFO  [org.infinispan.remoting.transport.jgroups.JGroupsTransport] (Incoming-10,shared=udp)
  ISPN000094: Received new cluster view: [node1/keycloak|1] (2) [node1/keycloak, node2/keycloak]
  ```

  If you see just one node mentioned, it’s possible that your cluster hosts are not joined together.

  Usually it’s best practice to have your cluster nodes on private network without firewall for communication among them. Firewall could be enabled just on public access point to your network instead. If for some reason you still need to have firewall enabled on cluster nodes, you will need to open some ports. Default values are UDP port 55200 and multicast port 45688 with multicast address 230.0.0.4. Note that you may need more ports opened if you want to enable additional features like diagnostics for your JGroups stack. Keycloak delegates most of the clustering work to Infinispan/JGroups. For more information, see [JGroups](http://docs.wildfly.org/16/High_Availability_Guide.html#JGroups_Subsystem) in the *WildFly 16 Documentation*.

- If you are interested in failover support (high availability), evictions, expiration and cache tuning, see [Server Cache Configuration](https://www.keycloak.org/docs/latest/server_installation/index.html#cache-configuration).

## 9. Server Cache Configuration

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/cache.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/cache.adoc)

Keycloak has two types of caches. One type of cache sits in front of the database to decrease load on the DB and to decrease overall response times by keeping data in memory. Realm, client, role, and user metadata is kept in this type of cache. This cache is a local cache. Local caches do not use replication even if you are in the cluster with more Keycloak servers. Instead, they only keep copies locally and if the entry is updated an invalidation message is sent to the rest of the cluster and the entry is evicted. There is separate replicated cache `work`, which task is to send the invalidation messages to the whole cluster about what entries should be evicted from local caches. This greatly reduces network traffic, makes things efficient, and avoids transmitting sensitive metadata over the wire.

The second type of cache handles managing user sessions, offline tokens, and keeping track of login failures so that the server can detect password phishing and other attacks. The data held in these caches is temporary, in memory only, but is possibly replicated across the cluster.

This chapter discusses some configuration options for these caches for both clustered a non-clustered deployments.

|      | More advanced configuration of these caches can be found in the [Infinispan](http://docs.wildfly.org/16/High_Availability_Guide.html#Infinispan_Subsystem) section of the *WildFly 16 Documentation*. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 9.1. Eviction and Expiration

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/cache/eviction.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/cache/eviction.adoc)

There are multiple different caches configured for Keycloak. There is a realm cache that holds information about secured applications, general security data, and configuration options. There is also a user cache that contains user metadata. Both caches default to a maximum of 10000 entries and use a least recently used eviction strategy. Each of them is also tied to an object revisions cache that controls eviction in a clustered setup. This cache is created implicitly and has twice the configured size. The same applies for the `authorization` cache, which holds the authorization data. The `keys` cache holds data about external keys and does not need to have dedicated revisions cache. Rather it has `expiration` explicitly declared on it, so the keys are periodically expired and forced to be periodically downloaded from external clients or identity providers.

The eviction policy and max entries for these caches can be configured in the *standalone.xml*, *standalone-ha.xml*, or*domain.xml* depending on your [operating mode](https://www.keycloak.org/docs/latest/server_installation/index.html#_operating-mode). In the configuration file, there is the part with infinispan subsystem, which looks similar to this:

```
<subsystem xmlns="urn:jboss:domain:infinispan:8.0">
    <cache-container name="keycloak">
        <local-cache name="realms">
            <object-memory size="10000"/>
        </local-cache>
        <local-cache name="users">
            <object-memory size="10000"/>
        </local-cache>
        ...
        <local-cache name="keys">
            <object-memory size="1000"/>
            <expiration max-idle="3600000"/>
        </local-cache>
        ...
    </cache-container>
```

To limit or expand the number of allowed entries simply add or edit the `object` element or the `expiration` element of particular cache configuration.

In addition, there are also separate caches `sessions`, `clientSessions`, `offlineSessions`, `offlineClientSessions`,`loginFailures` and `actionTokens`. These caches are distributed in cluster environment and they are unbounded in size by default. If they are bounded, it would then be possible that some sessions will be lost. Expired sessions are cleared internally by Keycloak itself to avoid growing the size of these caches without limit. If you see memory issues due to a large number of sessions, you can try to:

- Increase the size of cluster (more nodes in cluster means that sessions are spread more equally among nodes)
- Increase the memory for Keycloak server process
- Decrease the number of owners to ensure that caches are saved in one single place. See [Replication and Failover](https://www.keycloak.org/docs/latest/server_installation/index.html#_replication) for more details
- Disable l1-lifespan for distributed caches. See Infinispan documentation for more details
- Decrease session timeouts, which could be done individually for each realm in Keycloak admin console. But this could affect usability for end users. See [Timeouts](https://www.keycloak.org/docs/6.0/server_admin/#_timeouts) for more details.

There is an additional replicated cache, `work`, which is mostly used to send messages among cluster nodes; it is also unbounded by default. However, this cache should not cause any memory issues as entries in this cache are very short-lived.

### 9.2. Replication and Failover

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/cache/replication.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/cache/replication.adoc)

There are caches like `sessions`, `authenticationSessions`, `offlineSessions`, `loginFailures` and a few others (See [Eviction and Expiration](https://www.keycloak.org/docs/latest/server_installation/index.html#_eviction) for more details), which are configured as distributed caches when using a clustered setup. Entries are not replicated to every single node, but instead one or more nodes is chosen as an owner of that data. If a node is not the owner of a specific cache entry it queries the cluster to obtain it. What this means for failover is that if all the nodes that own a piece of data go down, that data is lost forever. By default, Keycloak only specifies one owner for data. So if that one node goes down that data is lost. This usually means that users will be logged out and will have to login again.

You can change the number of nodes that replicate a piece of data by change the `owners` attribute in the `distributed-cache` declaration.

owners

```
<subsystem xmlns="urn:jboss:domain:infinispan:8.0">
   <cache-container name="keycloak">
       <distributed-cache name="sessions" owners="2"/>
...
```

Here we’ve changed it so at least two nodes will replicate one specific user login session.

|      | The number of owners recommended is really dependent on your deployment. If you do not care if users are logged out when a node goes down, then one owner is good enough and you will avoid replication. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

|      | It is generally wise to configure your environment to use loadbalancer with sticky sessions. It is beneficial for performance as Keycloak server, where the particular request is served, will be usually the owner of the data from the distributed cache and will therefore be able to look up the data locally. See [Sticky sessions](https://www.keycloak.org/docs/latest/server_installation/index.html#sticky-sessions) for more details. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 9.3. Disabling Caching

To disable the realm or user cache, you must edit the `standalone.xml`, `standalone-ha.xml`, or `domain.xml` file in your distribution. The location of this file depends on your [operating mode](https://www.keycloak.org/docs/latest/server_installation/index.html#_operating-mode). Here’s what the config looks like initially.

```
    <spi name="userCache">
        <provider name="default" enabled="true"/>
    </spi>

    <spi name="realmCache">
        <provider name="default" enabled="true"/>
    </spi>
```

To disable the cache set the `enabled` attribute to false for the cache you want to disable. You must reboot your server for this change to take effect.

### 9.4. Clearing Caches at Runtime

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/cache/clear.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/cache/clear.adoc)

To clear the realm or user cache, go to the Keycloak admin console Realm Settings→Cache Config page. On this page you can clear the realm cache, the user cache or cache of external public keys.

|      | The cache will be cleared for all realms! |
| ---- | ----------------------------------------- |
|      |                                           |

## 10. Keycloak Security Proxy

[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_installation/topics/proxy.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_installation/topics/proxy.adoc)

Keycloak has an HTTP(S) proxy that you can put in front of web applications and services where it is not possible to install the Keycloak adapter. You can set up URL filters so that certain URLs are secured either by browser login and/or bearer token authentication. You can also define role constraints for URL patterns within your applications.

### 10.1. Proxy Install and Run

Download the Keycloak proxy distribution from the Keycloak download pages and unzip it.

```
$ unzip keycloak-proxy-dist.zip
```

To run it you must have a proxy config file (which we’ll discuss in a moment).

```
$ java -jar bin/launcher.jar [your-config.json]
```

If you do not specify a path to the proxy config file, the launcher will look in the current working directory for the file named `proxy.json`

### 10.2. Proxy Configuration

Here’s an example configuration file.

```
{
    "target-url": "http://localhost:8082",
    "target-request-timeout": "60000",
    "send-access-token": true,
    "bind-address": "localhost",
    "http-port": "8080",
    "https-port": "8443",
    "keystore": "classpath:ssl.jks",
    "keystore-password": "password",
    "key-password": "password",
    "applications": [
        {
            "base-path": "/customer-portal",
            "error-page": "/error.html",
            "adapter-config": {
                "realm": "demo",
                "resource": "customer-portal",
                "realm-public-key": "MIGfMA0GCSqGSIb",
                "auth-server-url": "http://localhost:8081/auth",
                "ssl-required" : "external",
                "principal-attribute": "name",
                "credentials": {
                    "secret": "password"
                }
            }
            ,
            "constraints": [
                {
                    "pattern": "/users/*",
                    "roles-allowed": [
                        "user"
                    ]
                },
                {
                    "pattern": "/admins/*",
                    "roles-allowed": [
                        "admin"
                    ]
                },
                {
                    "pattern": "/users/permit",
                    "permit": true
                },
                {
                    "pattern": "/users/deny",
                    "deny": true
                }
            ]
        }
    ]
}
```

#### 10.2.1. Basic Config

The basic configuration options for the server are as follows:

- target-url

  The URL this server is proxying. *REQUIRED*.

- target-request-timeout

  The timeout (in ms) for the proxied request. *OPTIONAL*. Default is 30000.

- send-access-token

  Boolean flag. If true, this will send the access token via the KEYCLOAK_ACCESS_TOKEN header to the proxied server. *OPTIONAL*. Default is false.

- bind-address

  DNS name or IP address to bind the proxy server’s sockets to. *OPTIONAL*. The default value is *localhost*

- http-port

  Port to listen for HTTP requests. If you do not specify this value, then the proxy will not listen for regular HTTP requests. *OPTIONAL*.

- https-port

  Port to listen for HTTPS requests. If you do not specify this value, then the proxy will not listen for HTTPS requests. *OPTIONAL*.

- keystore

  Path to a Java keystore file that contains private key and certificate for the server to be able to handle HTTPS requests. Can be a file path, or, if you prefix it with `classpath:` it will look for this file in the classpath. *OPTIONAL*. If you have enabled HTTPS, but have not defined a keystore, the proxy will auto-generate a self-signed certificate and use that.

- buffer-size

  HTTP server socket buffer size. Usually the default is good enough. *OPTIONAL*.

- buffers-per-region

  HTTP server socket buffers per region. Usually the default is good enough. *OPTIONAL*.

- io-threads

  Number of threads to handle IO. Usually default is good enough. *OPTIONAL*. The default is the number of available processors * 2.

- worker-threads

  Number of threads to handle requests. Usually the default is good enough. *OPTIONAL*. The default is the number of available processors * 16.

### 10.3. Application Config

Next under the `applications` array attribute, you can define one or more applications per host you are proxying.

- base-path

  The base context root for the application. Must start with '/'. *REQUIRED*.

- error-page

  If the proxy has an error, it will display the target application’s error page relative URL. *OPTIONAL*. This is a relative path to the base-path. In the example above it would be `/customer-portal/error.html`.

- adapter-config

  *REQUIRED*. Same configuration as any other Keycloak adapter.

- proxy-address-forwarding

  Enable usage of X-Forwarded-For, X-Forwarded-Host, X-Forwarded-Proto when hosted behind another proxy/load-balancer.

#### 10.3.1. Constraint Config

Next under each application you can define one or more constraints in the `constraints` array attribute. A constraint defines a URL pattern relative to the base-path. You can deny, permit, or require authentication for a specific URL pattern. You can specify roles allowed for that path as well. More specific constraints will take precedence over more general ones.

- pattern

  URL pattern to match relative to the base-path of the application. Must start with '/'. *REQUIRED.* You may only have one wildcard and it must come at the end of the pattern.Valid: `/foo/bar/*` and `/foo/*.txt`Not valid: `/*/foo/*`.

- roles-allowed

  Array of strings of roles allowed to access this url pattern. *OPTIONAL*.

- methods

  Array of strings of HTTP methods that will exclusively match this pattern and HTTP request. *OPTIONAL*.

- excluded-methods

  Array of strings of HTTP methods that will be ignored when match this pattern. *OPTIONAL*.

- deny

  Deny all access to this URL pattern. *OPTIONAL*.

- permit

  Permit all access without requiring authentication or a role mapping. *OPTIONAL*.

- permit-and-inject

  Permit all access, but inject the headers, if user is already authenticated. *OPTIONAL*.

- authenticate

  Require authentication for this pattern, but no role mapping. *OPTIONAL*.

#### 10.3.2. Header Names Config

Next under the list of applications you can override the defaults for the names of the header fields injected by the proxy (see [Keycloak Identity Headers](https://www.keycloak.org/docs/latest/server_installation/index.html#_identity_headers)). This mapping is optional.

- keycloak-subject

  e.g. MYAPP_USER_ID

- keycloak-username

  e.g. MYAPP_USER_NAME

- keycloak-email

  e.g. MYAPP_USER_EMAIL

- keycloak-name

  e.g. MYAPP_USER_ID

- keycloak-access-token

  e.g. MYAPP_ACCESS_TOKEN

### 10.4. Keycloak Identity Headers

When forwarding requests to the proxied server, Keycloak Proxy will set some additional headers with values from the OIDC identity token it received for authentication.

- KEYCLOAK_SUBJECT

  User id. Corresponds to JWT `sub` and will be the user id Keycloak uses to store this user.

- KEYCLOAK_USERNAME

  Username. Corresponds to JWT `preferred_username`.

- KEYCLOAK_EMAIL

  Email address of user if set.

- KEYCLOAK_NAME

  Full name of user if set.

- KEYCLOAK_ACCESS_TOKEN

  Send the access token in this header if the proxy was configured to send it. This token can be used to make bearer token requests. Header field names can be configured using a map of `header-names` in configuration file:`{     "header-names" {         "keycloak-subject": "MY_SUBJECT"     } }`

------

[1](https://www.keycloak.org/docs/latest/server_installation/index.html#_footnoteref_1). Tracked as <https://issues.jboss.org/browse/KEYCLOAK-3873>
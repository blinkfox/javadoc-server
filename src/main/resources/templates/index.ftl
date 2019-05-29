<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8"/>
    <title>JDoc | Javadoc托管服务</title>
    <link rel="stylesheet" href="${baseUrl}/lib/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="${baseUrl}/lib/bootstrap/docs.min.css">
    <link rel="stylesheet" href="${baseUrl}/css/jdoc.css">
</head>

<body>
<header class="navbar navbar-static-top bs-docs-nav" id="top">
    <div class="container">
        <div class="navbar-header">
            <button class="navbar-toggle collapsed" type="button" data-toggle="collapse" data-target="#bs-navbar" aria-controls="bs-navbar" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="/" class="navbar-brand">JDoc</a>
        </div>
        <nav id="bs-navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li>
                    <a href="/">首页</a>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="https://github.com/blinkfox/javadoc-server" target="_blank">源码</a></li>
            </ul>
        </nav>
    </div>
</header>

<div class="bs-docs-header" id="content" tabindex="-1">
    <div class="container">
        <h1>Javadoc 服务</h1>
        <p>这是一个用来托管和查看 Javadoc 的 SpringBoot 服务。当你输入 Maven 坐标时，会自动从 Maven 中央仓库拉取 Javadoc 资源，并存储到 MinIO 中，方便大家查看和使用。</p>
    </div>
</div>

<div class="container bs-docs-container">
    <h3 id="overview" class="page-header">尝试一下</h3>
    <p>请在下方的输入框中修改你需要查看的 javadoc.jar 包的 <code>groupId</code>、<code>artifactId</code>、和<code>version</code>，然后"点击查看"即可。</p>
    <div class="form-inline">
        <div class="form-group group-form-group">
            <div class="input-group">
                <div class="input-group-addon">groupId</div>
                <input type="text" class="form-control" id="groupId" value="com.blinkfox" placeholder="groupId">
            </div>
        </div>
        <div class="form-group artifact-form-group">
            <div class="input-group">
                <div class="input-group-addon">artifactId</div>
                <input type="text" class="form-control" id="artifactId" value="zealot" placeholder="artifactId">
            </div>
        </div>
        <div class="form-group version-form-group">
            <div class="input-group">
                <div class="input-group-addon">version</div>
                <input type="text" class="form-control" id="version" value="1.3.1" placeholder="version">
            </div>
        </div>
        <button class="btn btn-primary" onclick="seeJavaDoc();">点击查看</button>
        <button class="btn btn-default" onclick="clean();">清空</button>
        <span id="helpBlock" class="help-block"><b>注意</b>：如果该 javadoc.jar 是第一次被加载访问的话，需要等候一段时间。</span>
    </div>

    <h3 class="page-header">使用概述</h3>
    <ul>
        <li>只需要通过 URL 即可访问你需要的 javadoc 资源，如：<code>${baseUrl}/docs/{groupId}/{artifactId}/{version}</code></li>
        <li>可以通过徽章服务来引用对应的 javadoc 资源链接，
            如：<a href="${baseUrl}/docs/com.blinkfox/zealot/1.3.1"><img src="https://img.shields.io/badge/zealot-1.3.1-brightgreen.svg"></a></li>
    </ul>

    <h3 class="page-header">使用示例</h3>
    <ul>
        <li>
            SpringBoot: <a href="${baseUrl}/docs/org.springframework.boot/spring-boot-starter-parent/2.1.5.RELEASE">${baseUrl}/docs/org.springframework.boot/spring-boot-starter-parent/2.1.5.RELEASE</a>
            <a href="${baseUrl}/docs/org.springframework.boot/spring-boot-starter-parent/2.1.5.RELEASE"><img src="https://img.shields.io/badge/SpringBoot-2.1.5-brightgreen.svg" alt="SpringBoot"></a>
        </li>
        <li>
            Zealot: <a href="${baseUrl}/docs/org.apache.commons/commons-lang3/3.9">${baseUrl}/docs/org.apache.commons/commons-lang3/3.9</a>
            <a href="${baseUrl}/docs/org.apache.commons/commons-lang3/3.9"><img src="https://img.shields.io/badge/commons lang3-3.9-brightgreen.svg"></a>
        </li>
    </ul>
</div>

<footer class="bs-docs-footer">
    <div class="container">
        <ul class="bs-docs-footer-links">
            <li><a href="https://github.com/blinkfox/javadoc-server">GitHub 源码</a></li>
            <li><a href="">示例查看</a></li>
            <li><a href="">其他项目</a></li>
            <li><a href="">关于我</a></li>
        </ul>
        <p>本项目源码受 <a rel="license" href="https://github.com/blinkfox/javadoc-server/blob/master/LICENSE" target="_blank">Apache License 2.0</a>开源协议保护。</p>
    </div>
</footer>

<script src="${baseUrl}/lib/jquery/jquery.min.js"></script>
<script src="${baseUrl}/lib/bootstrap/bootstrap.min.js"></script>
<script>
    var isBlank = function (x) {
        return !x || x === '' || $.trim(x).length === 0;
    };

    var seeJavaDoc = function () {
        // 获取和判断 groupId.
        var groupId = $('#groupId').val();
        var $group = $('.group-form-group');
        if (isBlank(groupId)) {
            $group.addClass('has-error');
            return;
        }
        $group.removeClass('has-error');

        // 获取和判断 artifactId.
        var artifactId = $('#artifactId').val();
        var $artifact = $('.artifact-form-group');
        if (isBlank(artifactId)) {
            $artifact.addClass('has-error');
            return;
        }
        $artifact.removeClass('has-error');

        // 获取和判断 version.
        var version = $('#version').val();
        var $version = $('.version-form-group');
        if (isBlank(version)) {
            $version.addClass('has-error');
            return;
        }
        $version.removeClass('has-error');

        window.open('${baseUrl}/docs/' + $.trim(groupId) +'/' + $.trim(artifactId) + '/' + $.trim(version), '_blank').location;
    };

    $(function () {
        $('.bs-docs-container .form-inline').keyup(function (event) {
            if (event.keyCode === 13) {
                seeJavaDoc();
            }
        });
    });
</script>
</body>
</html>
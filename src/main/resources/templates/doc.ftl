<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8"/>
    <title>Javadoc 托管服务.</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css">
    <style>
        .breadcrumb {
            margin-bottom: 0px;
        }

        .breadcrumb a {
            color: #4A6782
        }

        #doc-iframe {
            width: 100%;
            height: calc(100vh - 50px);
        }
    </style>
</head>

<body>

<div>
    <ol class="breadcrumb">
        <li><a href="/">首页</a></li>
        <li>${groupId}</li>
        <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                ${artifactId} <i class="caret"></i>
            </a>
            <ul class="dropdown-menu">
                <#if artifactIds??>
                <#list artifactIds as afId>
                <li><a href="/docs/${groupId}/${afId}">${afId}</a></li>
                </#list>
                </#if>
            </ul>
        </li>
        <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                ${version} <i class="caret"></i>
            </a>
            <ul class="dropdown-menu">
                <#if versions??>
                <#list versions as v>
                <li><a href="/docs/${groupId}/${artifactId}/${v}">${v}</a></li>
                </#list>
                </#if>
            </ul>
        </li>
    </ol>
    <div id="doc-container">
        <div id="info-tip" class="alert alert-info hide" role="alert"></div>
        <div id="warn-tip" class="alert alert-warning hide" role="alert"></div>
        <div id="error-tip" class="alert alert-danger hide" role="alert"></div>
        <iframe id="doc-iframe" class="hide" src=""></iframe>
    </div>
</div>

<script src="https://cdn.bootcss.com/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"></script>
<script>
    var groupId = '${groupId}';
    var artifactId = '${artifactId}';
    var version = '${version}';
    var flag = true;

    /**
     * 校验参数信息.
     */
    var validParams = function () {
        if (!version) {
            $('#error-tip').text('请填写该 jar 对应版本 version 的值!').removeClass('hide');
            return false;
        }

        $('#info-tip').text('正在加载 Javadoc 资源.').removeClass('hide');
        return true;
    };

    /**
     * 获取 javadoc 的 url.
     */
    var getJavadocUrl = function() {
        if (!validParams()) {
            return;
        }

        $.ajax({
            url: '/javadoc/' + groupId + '/' + artifactId + '/' + version,
            success: function(result) {
                $('#info-tip').text('').addClass('hide');
                $('#warn-tip').text('').addClass('hide');
                $('#error-tip').text('').addClass('hide');
                $('#doc-iframe').attr('src', result).removeClass('hide');
            },
            error: function() {
                $('#info-tip').text('').addClass('hide');
                $('#error-tip').text('加载 Javadoc 资源失败，请检查 jar 参数信息是否正确！').removeClass('hide');
            }
        });
    };

    getJavadocUrl();
</script>
</body>
</html>
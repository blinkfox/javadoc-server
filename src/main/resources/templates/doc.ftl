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
                <li><a href="/doc/com.blinkfox/adept">adept</a></li>
                <li><a href="/doc/com.blinkfox/jpack-maven-plugin">jpack-maven-plugin</a></li>
                <li><a href="/doc/com.blinkfox/mini-table">mini-table</a></li>
                <li><a href="/doc/com.blinkfox/zealot">zealot</a></li>
                <li><a href="/doc/com.blinkfox/zealot-spring-boot-starter">zealot-spring-boot-starter</a></li>
            </ul>
        </li>
        <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                ${version} <i class="caret"></i>
            </a>
            <ul class="dropdown-menu">
                <li><a href="/doc/com.blinkfox/adept/1.0.0">1.0.0</a></li>
            </ul>
        </li>
    </ol>
    <div id="doc-container">
        <iframe id="doc-iframe" src="http://127.0.0.1:9000/docs/com/blinkfox/zealot/1.3.1/index.html"></iframe>
    </div>
</div>

<script src="https://cdn.bootcss.com/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"></script>
<script>
    var groupId = '${groupId}';
    var artifactId = '${artifactId}';
    var version = '${version}';

    /**
     * 获取 javadoc 的 url.
     */
    var getJavadocUrl = function() {
        $.ajax({
            url: '/javadoc/' + groupId + '/' + artifactId + '/' + version,
            success: function(result) {
                console.log('执行成功 success.');
                var docFrame = '<iframe id="doc-iframe" src="' + result + '"></iframe>';
                console.log(docFrame);
                $('#doc-container').append(docFrame);
            },
            error: function() {
                alert('执行失败!');
            }
        });
    };

    //getJavadocUrl();
</script>
</body>
</html>
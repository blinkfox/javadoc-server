<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8"/>
    <title>Javadoc 托管服务.</title>
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <style>
        #doc-iframe {
            width: 100%;
            height: calc(100vh - 56px);
        }
    </style>
</head>

<body>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">Javadoc</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNavDropdown">
        <ul class="navbar-nav">
            <li class="nav-item active">
                <a class="nav-link" href="#">${groupId} <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="artifactIdDropdownMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    ${artifactId}
                </a>
                <div class="dropdown-menu" aria-labelledby="artifactIdDropdownMenu">
                    <a class="dropdown-item" href="#">Action</a>
                    <a class="dropdown-item" href="#">Another action</a>
                    <a class="dropdown-item" href="#">Something else here</a>
                </div>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="versionDropdownMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    ${version}
                </a>
                <div class="dropdown-menu" aria-labelledby="versionDropdownMenu">
                    <a class="dropdown-item" href="#">Action</a>
                    <a class="dropdown-item" href="#">Another action</a>
                    <a class="dropdown-item" href="#">Something else here</a>
                </div>
            </li>
            <button id="download-jar" class="btn btn-outline-dark btn-sm">下 载</button>
        </ul>
    </div>
</nav>

<div id="doc-container"></div>

<script src="https://cdn.bootcss.com/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/popper.js/1.12.9/umd/popper.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
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

    getJavadocUrl();
</script>
</body>
</html>
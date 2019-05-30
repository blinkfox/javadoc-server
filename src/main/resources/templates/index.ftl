<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8"/>
    <title>JDoc | Javadoc托管服务</title>
    <link rel="Shortcut Icon" href="${baseUrl}/images/favicon.ico">
    <link rel="stylesheet" href="${baseUrl}/lib/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="${baseUrl}/lib/bootstrap/docs.min.css">
    <link rel="stylesheet" href="${baseUrl}/lib/prism/prism.css">
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
            <a href="${baseUrl}" class="navbar-brand">JDoc</a>
        </div>
        <nav id="bs-navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li>
                    <a href="${baseUrl}">首页</a>
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
        <button class="btn btn-success" onclick="seeJavaDoc();">点击查看</button>
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
            spring-context: <a href="${baseUrl}/docs/org.springframework/spring-context/5.1.7.RELEASE">${baseUrl}/docs/org.springframework/spring-context/5.1.7.RELEASE</a>
            <a href="${baseUrl}/docs/org.springframework/spring-context/5.1.7.RELEASE"><img src="https://img.shields.io/badge/SpringBoot-2.1.5-brightgreen.svg" alt="SpringBoot"></a>
        </li>
        <li>
            commons-lang3: <a href="${baseUrl}/docs/org.apache.commons/commons-lang3/3.9">${baseUrl}/docs/org.apache.commons/commons-lang3/3.9</a>
            <a href="${baseUrl}/docs/org.apache.commons/commons-lang3/3.9"><img src="https://img.shields.io/badge/commons lang3-3.9-brightgreen.svg"></a>
        </li>
    </ul>

    <h3 class="page-header">生成 Javadoc 的 Maven 插件</h3>
    <pre class="language-xml"><code class="language-xml">
&lt;plugin&gt;
    &lt;groupId&gt;org.apache.maven.plugins&lt;/groupId&gt;
    &lt;artifactId&gt;maven-javadoc-plugin&lt;/artifactId&gt;
    &lt;version&gt;3.0.1&lt;/version&gt;
    &lt;executions&gt;
        &lt;execution&gt;
            &lt;phase&gt;package&lt;/phase&gt;
            &lt;goals&gt;
                &lt;goal&gt;jar&lt;/goal&gt;
            &lt;/goals&gt;
        &lt;/execution&gt;
    &lt;/executions&gt;
&lt;/plugin&gt;</code></pre>

    <h3 class="page-header">Javadoc 规范</h3>
    <h4>1 格式</h4>
    <h5>1.1 一般形式</h5>
    <p>Javadoc块的基本格式如下所示：</p>
    <pre class=" language-java"><code class="language-java"><span class="token comment" spellcheck="true">/**
 * Multiple lines of Javadoc text are written here,
 * wrapped normally...
 */</span>
<span class="token keyword">public</span> <span class="token keyword">int</span> <span class="token function">method</span><span class="token punctuation">(</span>String p1<span class="token punctuation">)</span> <span class="token punctuation">{</span> <span class="token punctuation">.</span><span class="token punctuation">.</span><span class="token punctuation">.</span> <span class="token punctuation">}</span></code></pre>
    <p>或者是以下单行形式：</p>
    <pre class=" language-java"><code class="language-java"><span class="token comment" spellcheck="true">/** An especially short bit of Javadoc. */</span></code></pre>
    <p>基本格式总是可以接受的。当整个<code>Javadoc</code>块能容纳于一行时(且没有标记<code>@XXX</code>)，就可以使用单行形式。</p>
    <h5>1.2 段落</h5>
    <p>空行(只包含最左侧星号的行)会出现在段落之间和<code>Javadoc</code>标记(<code>@XXX</code>)之前(如果有的话)。 除了第一个段落，每个段落第一个单词前都有标签<code>&lt;p&gt;</code>，并且它和第一个单词间没有空格。</p>
    <h5>1.3 Javadoc标记</h5>
    <p>标准的<code>Javadoc</code>标记按以下顺序出现：<code>@param</code>, <code>@return</code>, <code>@throws</code>, <code>@deprecated</code>, 前面这4种标记如果出现，描述都不能为空。 当描述无法在一行中容纳，连续行需要至少再缩进<code>4</code>个空格(<strong>注</strong>：如果你的缩进统一采用采用<code>4</code>个空格，那么这里就应该是<code>8</code>个空格)。</p>
    <h4>2 摘要片段</h4>
    <p>每个类或成员的<code>Javadoc</code>以一个简短的摘要片段开始。这个片段是非常重要的，在某些情况下，它是唯一出现的文本，比如在类和方法索引中。</p>
    <p>这只是一个小片段，可以是一个名词短语或动词短语，但不是一个完整的句子。它不会以<code>A {@code Foo} is a...</code>或者<code>This method returns...</code>开头, 它也不会是一个完整的祈使句，如<code>Save the record.</code>。然而，由于开头大写及被加了标点，它看起来就像是个完整的句子。</p>
    <blockquote>
        <p><strong>注意</strong>：一个常见的错误是把简单的Javadoc写成<code>/** @return the customer ID */</code>，这是不正确的。它应该写成<code>/** Returns the customer ID. */</code>。</p>
    </blockquote>
    <h4>3 在哪里使用Javadoc</h4>
    <p>至少在每个<code>public</code>类及它的每个<code>public</code>和<code>protected</code>成员处使用<code>Javadoc</code>，以下是一些例外：</p>
    <h5>3.1 例外：不言自明的方法</h5>
    <p>对于简单明显的方法如<code>getFoo</code>，<code>Javadoc</code>是可选的(可以不写)。这种情况下除了写<code>Returns the foo</code>，确实也没有什么值得写了。</p>
    <p>单元测试类中的测试方法可能是不言自明的最常见例子了，我们通常可以从这些方法的描述性命名中知道它是干什么的，因此不需要额外的文档说明。</p>
    <blockquote>
        <p><strong>注意</strong>：如果有一些相关信息是需要读者了解的，那么以上的例外不应作为忽视这些信息的理由。例如，对于方法名<code>getCanonicalName</code>，就不应该忽视文档说明，因为读者很可能不知道词语<code>canonical name</code>指的是什么。</p>
    </blockquote>
    <h5>3.2 例外：重载</h5>
    <p>如果一个方法重载了超类中的方法，那么<code>Javadoc</code>并非必需的。</p>
    <h5>3.3 可选的Javadoc</h5>
    <p>对于包外不可见的类和方法，如有需要，也是要使用<code>Javadoc</code>的。如果一个注释是用来定义一个类，方法，字段的整体目的或行为， 那么这个注释应该写成<code>Javadoc</code>，这样更统一更友好。</p>
    <p>本规范的原文地址: <a href="http://checkstyle.sourceforge.net/reports/google-java-style-20170228.html" target="_blank" rel="noopener">Google Java Style Guide</a></p>
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
<script src="${baseUrl}/lib/prism/prism.js"></script>
<script>
    var isBlank = function (x) {
        return !x || x === '' || $.trim(x).length === 0;
    };

    var clean = function () {
        $('#groupId').val('');
        $('#artifactId').val('');
        $('#version').val('');
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
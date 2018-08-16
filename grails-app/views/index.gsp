<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Grails</title>

    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
</head>
<body>

    <div class="svg" role="presentation">
        <div class="grails-logo-container">
            <asset:image src="grails-cupsonly-logo-white.svg" class="grails-logo"/>
        </div>
    </div>

    <div id="content" role="main">
        <section class="row colset-2-its">
            <h1>Welcome to Robot Web API</h1>

            <div id="controllers" role="navigation" style="text-align: center">
                <h2>Available APIs:</h2>
                <ul>
                    <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                        <li class="controller" style="list-style: none">
                            <g:link controller="${c.logicalPropertyName}">${c.name}</g:link>
                        </li>
                    </g:each>
                </ul>
            </div>
        </section>
    </div>

</body>
</html>

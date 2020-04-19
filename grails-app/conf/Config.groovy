// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
	all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
	atom:          'application/atom+xml',
	css:           'text/css',
	csv:           'text/csv',
	form:          'application/x-www-form-urlencoded',
	html:          ['text/html','application/xhtml+xml'],
	js:            'text/javascript',
	json:          ['application/json', 'text/json'],
	multipartForm: 'multipart/form-data',
	rss:           'application/rss+xml',
	text:          'text/plain',
	hal:           ['application/hal+json','application/hal+xml'],
	xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
	views {
		gsp {
			encoding = 'UTF-8'
			htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
			codecs {
				expression = 'html' // escapes values inside ${}
				scriptlet = 'html' // escapes output from scriptlets in GSPs
				taglib = 'none' // escapes output from taglibs
				staticparts = 'none' // escapes output from static template parts
			}
		}
		// escapes all not-encoded output at final stage of outputting
		// filteringCodecForContentType.'text/html' = 'html'
	}
}

grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

grails.gorm.failOnError=true

def logFilePath

environments {
	development {
		grails.logging.jul.usebridge = true
		logFilePath = "C:/Development/Java/grails/logs/turmoil.log"
	}
	test {
		grails.logging.jul.usebridge = true
		logFilePath = "C:/Development/Java/grails/logs/turmoil-test.log"
	}
	production {
		grails.logging.jul.usebridge = false

		if (System.properties['os.name'].toLowerCase().contains('windows'))
		{
			logFilePath = "C:/Development/Java/grails/logs/turmoil-production.log"
		}
		else
		{
			logFilePath = "/tmp/turmoil/logs/turmoil.log"
		}

		// TODO: grails.serverURL = "http://www.changeme.com"
	}
}

// log4j configuration
log4j = {
	def loggerPattern = '%d %-5p >> %m%n'

	appenders {
		appenders {
			console name:'stdout', layout:pattern(conversionPattern: loggerPattern)
			rollingFile name: "file", maxFileSize: 1024*1024, file: logFilePath, layout:pattern(conversionPattern: loggerPattern)
		}
	}

	error	'org.codehaus.groovy.grails.web.servlet',  //  controllers
			'org.codehaus.groovy.grails.web.pages', //  GSP
			'org.codehaus.groovy.grails.web.sitemesh', //  layouts
			'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
			'org.codehaus.groovy.grails.web.mapping', // URL mapping
			'org.codehaus.groovy.grails.commons', // core / classloading
			'org.codehaus.groovy.grails.plugins', // plugins
			'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
			'org.springframework',
			'org.hibernate',
			'net.sf.ehcache.hibernate'

	warn	'org.mortbay.log'

	debug	'grails.app.conf',
			'grails.app.filters',
			'grails.app.taglib',
			'grails.app.services',
			'grails.app.controllers',
			'grails.app.domain'

	info	'grails.app',
			'turmoil'

	root {
		info 'file', 'stdout'
		additivity = true
	}

}

grails.plugins.remotepagination.max = 10

log4ja = {
	// Example of changing the log pattern for the default console appender:
	//
	//appenders {
	//    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
	//}
	def loggerPattern = '%d %-5p >> %m%n'

	/*
	appenders {
		file name:'file', file:'E:/Development/Java/grails/logs/turmoil.log', append: true
		console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
	}
	*/

	appenders {
		console name:'stdout', layout:pattern(conversionPattern: loggerPattern)
		rollingFile name: "file", maxFileSize: 1024*1024, file: logFilePath, layout:pattern(conversionPattern: loggerPattern)
	}

	def errorClasses = ['grails.app', 'turmoil.*'] // add more classes if needed
	def infoClasses = ['grails.app', 'turmoil.*'] // add more classes if needed
	def debugClasses = ['grails.app', 'turmoil.*'] // add more classes if needed

	error 'org.codehaus.groovy.grails.web.servlet',        // controllers
		'org.codehaus.groovy.grails.web.pages',          // GSP
		'org.codehaus.groovy.grails.web.sitemesh',       // layouts
		'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
		'org.codehaus.groovy.grails.web.mapping',        // URL mapping
		'org.codehaus.groovy.grails.commons',            // core / classloading
		'org.codehaus.groovy.grails.plugins',            // plugins
		'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
		'org.springframework',
		'org.hibernate',
		'net.sf.ehcache.hibernate',
		'turmoil',
		'turmoil.*',
		'turmoil.helpers.ItemTemplatesHelper'

	error	stdout: errorClasses //, file: errorClasses
	info	stdout: infoClasses  //, file: infoClasses
	debug	stdout: debugClasses //, file: debugClasses

	// Set level for all application artifacts
	info "grails.app"

	root {
		info 'stdout', 'file'
		debug 'file'
		error 'stdout', 'file'
		additivity = true
	}

	/*
	environments {

		test {
			root {
				error 'stdout'
			}
			debug 'grails.app'
		}

		development {
			root {
				error 'stdout'
			}
			debug 'grails.app'
		}
	}
	*/
}
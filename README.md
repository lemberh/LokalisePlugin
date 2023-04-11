
# LokalisePlugin

This is a simple plugin which helps with the integration of [Lokalise](https://lokalise.co) localization service into Android App. 
This plugin has two main purposes:
* upload local project strings to Lokalise backend
* update local project strings files with latest translations from Lokalise backend

## Getting Started
### Setup

To enable this plugin, add the following lines in your project `build.gradle`

```
buildscript {  
  repositories {  
        .... 
        maven { url 'https://jitpack.io' }  // you need Jitpack repository
  }  
  
  dependencies {  
         .......
         classpath 'com.github.OiTchau:LokalisePlugin:1.1.0' // latest version of plugin goes here
  }  
}
```
And in your app module `build.gradle` add this:

```
apply plugin: 'org.rnazarevych.lokalise-plugin' // apply plugin 
......
// and configure it
lokalise {  
    api {  
        projectId = "123456789" // t
        token = "token"  // you need token with read and write permissions
    }  
  
    translationsUpdateConfig {  
        resPath = "$rootDir/app/src/main/res" // path to project res folder
        lokaliseLangs = ["en", "es_AR", "es_AR"]
        androidLangs = ["en", "es-rAR", "es"]  
        defaultLang = "en"  // default language which will be put into values/strings.xml
    }  
  
    //Here you can specify which files you want to upload. convertPlaceholders is true by default
    uploadEntry {  
        path = "$rootDir/app/src/main/res/values/strings.xml"  
        lang = "en"
        convertPlaceholders = false
    }  
    uploadEntry {  
        path = "$rootDir/app/src/main/res/values-pt/strings.xml"  
        lang = "pt"  
    }  
}
```
To get `projectId` go to your Project/Settings/General 
To get read/write `token` read this [documentation](https://docs.lokalise.co/faqs/api-tokens)

After this configuration you should be able to use those 2 gradle tasks `uploadStrings` and `downloadTranslations`  you can execute them from IDE GUI or command line.
``` ./gradlew downloadTranslations ```

Or you can, for example, create a Gradle task which will download translations and build APK
```
task buildRelease(type: GradleBuild) {  
    tasks = ['clean','uploadStrings', 'downloadTranslations', 'assembleRelease', 'bundleRelease']  
}
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

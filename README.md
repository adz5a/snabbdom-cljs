# boot-cljsjs

<img src="https://dl.dropboxusercontent.com/u/453692/cljsjs-logo.png"
  alt="CLJSJS logo" align="right" />

[![Clojars Project](http://clojars.org/cljsjs/boot-cljsjs/latest-version.svg)](http://clojars.org/cljsjs/boot-cljsjs)

This project provides tasks for the [boot][boot] build system to
package and use Javascript dependencies in Clojurescript projects.

- [cljsjs.boot-cljsjs][main-ns] provides tasks to be used in application
  project to add JS files from diffrent sources to fileset so they can
  be used by [boot-cljs][boot-cljs].
- [cljsjs.boot-cljsjs.packaging][packaging-ns] provides tasks to help
  packaging of libraries for cljsjs.

## Using libraries

If you are using [boot-cljs][boot-cljs] you might want to use external
JS libraries with your project. Using boot-cljsjs you can add those
external files from jar files instead of copying them to your project.

Using boot-cljsjs you can use JS libraries from three sources in your
project.

1. Cljsjs packaged jars
  - When using Cljsjs jars the transitive dependencies are added automatically to fileset
2. Webjars
3. Any jar-files containing js-files

For each source we have own task used to bring files from the source
to the fileset.

1. `from-cljsjs`
  - This task will go through all jars in your classpath and find
    files in cljsjs prefix.
  - All files will be copied to filesset
2. `from-webjars`
  - This task will go through all jars in your classpath and find
    files in webjars prefix.
  - You need to provide name for file you want to import,
    e.g. "momentjs/moment.js"
    - This would import e.g. file
      "META-INF/resources/webjars/momentjs/2.8.3/moment.js" depending
      on version etc of the dependancy.
  - To import multiple files from Webjars, you would call this task
    multiple times.
3. `from-jars`
  - This task will just copy a file of given path from classpath. In
  effect this just calls `(io/resource path)` and copies found file to
  fileset.

### Why this works

[Boot-cljs][boot-cljs] will search fileset for files ending in
`.inc.js`, `.ext.js` and `.lib.js` and uses the found files for cljs
compiler options.

In any case, on your html code you only need to include one source
tag.  This works because in `:advanced` mode all files are
concatenated into one.  In development mode you'll be using
`:optimizations :none` and `:unified-mode true` and in that case
[boot-cljs][boot-cljs] will write a shim JS which will load your all
external libraries.

NOTE: In development mode the target file should by inside your
docroot so the files are accessible through your webserver.

### Transitive dependencies

Using Cljsjs jars transitive dependencies can automatically be added to fileset and thus to cljs build.
For example if your application depends on a Cljs library which depends on `cljsjs/momentjs` the only code you
need to add to your project is call to call `from-cljsjs` task. Because we are using Maven dependencies if you need, you can depend on `cljsjs/momentjs` from your own project if you e.g. need to use a new version.

Cljsjs jars include both development (non-minified) and production (minified) versions and you can select one being used by `from-cljsjs` `:profile` option.

### Example

This example illustrates how you could use `cljsjs/react` in your Reagent project:

```clj
;; in your build.boot file:
(set-env!
  :source-paths #{"src"}
  :dependencies '[[adzerk/boot-cljs   "0.0-2629-1" :scope "test"]
                  [cljsjs/boot-cljsjs "0.4.0"      :scope "test"]
                  [cljsjs/react       "0.12.2-3"]
                  [reagent            "0.4.3"]]

(require '[adzerk.boot-cljs   :refer [cljs]]
         '[cljsjs.boot-cljsjs :refer [from-cljsjs]])


; Below two tasks are used:
; - `from-cljsjs` imports files from cljsjs jars into the fileset,
;   which will be passed to the next task. The `:profile` option
;   allows you to choose between production and development assets.
; - `cljs` compiles your Clojurescript code. The `cljs` task will
;   handle files in the fileset ending in `.inc.js`, `.ext.js` and
;   `.lib.js` using them as preamble, externs and library files
;   respectively.

(deftask build-dev []
  (comp
    (from-cljsjs :profile :development)
    (cljs :optimizations :none)))

(deftask build-prod []
  (comp
    (from-cljsjs :profile :production)
    (cljs :optimizations :advanced)))
```

## Packaging jars

Instructions on how to package CLJSJS jars can be found in the [packages project][cljsjs-packages].

# License

Copyright © 2014 Martin Klepsch and Juho Teperi

Distributed under the Eclipse Public License, the same as Clojure.

[boot]: https://github.com/boot-clj/boot
[cljsjs-packages]: https://github.com/cljsjs/packages
[packaging-ns]: src/cljsjs/boot_cljsjs/packaging.clj
[main-ns]: src/cljsjs/boot_cljsjs.clj
[boot-cljs]: https://github.com/adzerk/boot-cljs
[cljsjs-react]: https://github.com/cljsjs/packages/tree/master/react

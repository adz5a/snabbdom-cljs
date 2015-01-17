(set-env!
  :source-paths #{"src"}
  :dependencies '[[adzerk/bootlaces "0.1.9" :scope "test"]])

(require '[adzerk.bootlaces :refer :all])

(def +version+ "0.4.0")

(bootlaces! +version+)

(task-options!
  pom  {:project     'cljsjs/boot-cljsjs
        :version     +version+
        :description "Tooling to package and deploy Javascript
                      libraries for Clojurescript projects"
        :url         "https://github.com/cljsjs/boot-cljsjs"
        :scm         {:url "https://github.com/cljsjs/boot-cljsjs"}
        :license     {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})

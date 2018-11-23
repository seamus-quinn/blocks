(defproject server "0.1.0-SNAPSHOT"
  :description "A graphql server for the blocks client"
  :url "https://github.com/seamus-quinn/blocks"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [com.walmartlabs/lacinia-pedestal "0.5.0"]
                 [io.aviso/logging "0.2.0"]])

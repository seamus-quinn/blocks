(ns server.schema
  "Contains custom resolvers and a function to provide the full schema."
  (:require
    [clojure.java.io :as io]
    [com.walmartlabs.lacinia.util :as util]
    [com.walmartlabs.lacinia.schema :as schema]
    [clojure.edn :as edn]))

(defn paths-resolver [context args value]
  [{:id 10 :name "Journaling" :description "blah"}
   {:id 14 :name "Daily run" :description "blahblah"}]
  )

(defn resolver-map
  []
  {:query/paths-by-user-id paths-resolver
   })

(defn load-schema
  []
  (-> (io/resource "blocks-schema.edn")
      slurp
      edn/read-string
      (util/attach-resolvers (resolver-map))
      schema/compile))

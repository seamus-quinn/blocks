(ns user
  (:require
    [server.schema :as s]
    [com.walmartlabs.lacinia :refer [execute]]))

(def schema (s/load-schema))

(defn q
  [query-string]
  (execute schema query-string nil nil))

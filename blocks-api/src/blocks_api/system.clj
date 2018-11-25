(ns blocks-api.system
  (:require
    [com.stuartsierra.component :as component]
    [blocks-api.server :as server]
    [blocks-api.db :as db]
    [blocks-api.schema :as schema]))

(defn new-system []
  (merge (component/system-map)
         (server/new-server)
         (schema/new-schema-provider)
         (db/new-db)))

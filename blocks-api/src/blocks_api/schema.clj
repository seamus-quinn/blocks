(ns blocks-api.schema
  "Contains custom resolvers and a function to provide the full schema."
  (:require
    [clojure.java.io :as io]
    [com.walmartlabs.lacinia.util :as util]
    [com.walmartlabs.lacinia.schema :as schema]
    [com.walmartlabs.lacinia.resolve :refer [resolve-as]]
    [com.stuartsierra.component :as component]
    [blocks-api.db :as db]
    [clojure.edn :as edn]))

(defn resolve-user-paths
  [db]
  (fn [_ {:keys [user_id]} {:keys [id]}]
    (db/list-paths-for-user db (Integer. (or user_id id)))))

(defn resolve-path-user
  [db]
  (fn [c a {:keys [user_id]}]
    (let [user (db/find-user db user_id)]
      (if (nil? user)
        (resolve-as nil {:message "User not found"
                         :status 404})
        user))))

(defn resolve-user
  [db]
  (fn  [c {:keys [id]} v]
    (let [user (db/find-user db id)]
      (if (nil? user)
        (resolve-as nil {:message "User not found"
                         :status 404})
        user))))

(defn insert-user
  [db]
  (fn [c {:keys [username password]} v]
    (cond 
      (-> password count (< 8))
      (resolve-as nil {:message "Password must be at least 8 characters"
                       :status 400})

      :else
      (do (db/insert-user db {:username username :password password})))))

(defn resolve-users [db] (fn [_ _ _] (db/list-users db)))

(defn- coll-to-map [xs] (reduce #(assoc %1 (:id %2) %2) {} xs))

(defn resolver-map
  [component]
  (let [db (:db component)]
    {:query/paths-by-user-id (resolve-user-paths db)
     :query/user-by-id (resolve-user db)
     :query/users (resolve-users db)
     :mutation/insert-user (insert-user db)
     :Path/user (resolve-path-user db)
     :User/paths (resolve-user-paths db)}))

(defn load-schema
  [component]
  (-> (io/resource "blocks-schema.edn")
      slurp
      edn/read-string
      (util/attach-resolvers (resolver-map component))
      schema/compile))

(defrecord SchemaProvider [schema]
  component/Lifecycle
  
  (start [this]
    (assoc this :schema (load-schema this)))

  (stop [this]
    (assoc this :schema nil)))

(defn new-schema-provider
  []
  {:schema-provider (-> {}
                        map->SchemaProvider
                        (component/using [:db]))})

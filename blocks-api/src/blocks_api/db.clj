(ns blocks-api.db
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [com.stuartsierra.component :as component]))

(defrecord BlocksDb [data]
  component/Lifecycle
  
  (start [this]
    (assoc this :data (-> (io/resource "blocks-data.edn")
                          slurp
                          edn/read-string
                          atom)))
  (stop [this]
    (assoc this :data nil)))

(defn new-db []
  {:db (map->BlocksDb {})})

(defn find-by-id [resource]
  (fn [db id]
    (let [id-int (Integer. id)]
      (->> db
           :data
           deref
           resource
           (filter #(= id-int (:id %)))
           first))))

(def find-user (find-by-id :users))
(def find-path (find-by-id :paths))

(defn list-paths-for-user
  [db user-id]
  (->> db
       :data
       deref
       :paths
       (filter #(= user-id (:user_id %)))))

(defn insert-user
  [db props]
  (let [db-atom (:data db)
        users (->>
                db-atom
                deref
                :users)
        next-id (->> users
                     (map :id)
                     (apply max)
                     inc)]
    (swap! db-atom update :users conj (merge {:id next-id} props))))

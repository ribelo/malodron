(ns malodron.db.events
  (:require [re-frame.core :as rf]
            [malodron.db.core :as db]
            [malodron.salesroom.db :refer [salesroom-state]]))


(rf/reg-event-db
  :db/initialize-db
  (fn [_ _]
    (let [db (merge db/default-db
                    salesroom-state)]
      (if-let [storage (db/load-local-storage)]
        (merge db storage)
        db))))

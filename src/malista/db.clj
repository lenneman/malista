(ns malista.db
  (:require [clojure.java.jdbc :as jdbc]))

(def db-spec {:dbtype "h2" :dbname "./malista"})

(defn add-new-player-to-db
  [fname sname sex]
  (let [is-male (if (= sex "male")
                  true
                  false)
        results (jdbc/insert! db-spec :players {:firstname fname :surname sname :sex_male is-male})]
    (assert (= (count results) 1))
    (first (vals (first results)))))

(defn get-all-players
  []
  (jdbc/query db-spec "SELECT * FROM players ORDER BY surname"))


(defn get-all-match-results
  []
  (jdbc/query db-spec (str "SELECT MATCH_ID, "
                           "MATCH_DATE, "
                           "PLAYER1, "
                           "PLAYER2, "
                           "OPPONENT1, "
                           "OPPONENT2, "
                           "RESULT_SET1, "
                           "RESULT_SET2, "
                           "RESULT_SET3 "
                           "FROM MATCH_RESULTS")))

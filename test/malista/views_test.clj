(ns my-webapp.views-test
  (:require [clojure.test :refer :all]
            [my-webapp.views :refer :all]))

(deftest test-get-player-id-from-name

  ;; ====================
  (testing "good input"
    (let [players (list {:playerid 3, :firstname "Sven",     :surname "Birnfeld",  :sex_male true}
                        {:playerid 1, :firstname "André",    :surname "Lehmann",   :sex_male true}
                        {:playerid 2, :firstname "Anja",     :surname "Lehmann",   :sex_male false}
                        {:playerid 7, :firstname "Surender", :surname "Medipally", :sex_male true}
                        {:playerid 5, :firstname "Janine",   :surname "Nordmann",  :sex_male false}
                        {:playerid 4, :firstname "Vinatha",  :surname "Pesari",    :sex_male false}
                        {:playerid 6, :firstname "Kevin",    :surname "Werner",    :sex_male true})]
      (is (= 1 (get-player-id-from-name players "Lehmann" "André")))
      (is (= 2 (get-player-id-from-name players "Lehmann" "Anja")))
      (is (= 6 (get-player-id-from-name players "Werner" "Kevin")))
      (is (= 7 (get-player-id-from-name players "Medipally" "Surender")))
      (is (= nil (get-player-id-from-name players "Mustermann" "Max")))
      ))

  (testing "bad input"
    (is (= nil (get-player-id-from-name '() "Mustermann" "Max")))
    (is (= nil (get-player-id-from-name '() "" "Max")))
    (is (= nil (get-player-id-from-name '() "Mustermann" "")))
    (is (= nil (get-player-id-from-name '({:firstname "Surender", :surname "Medipally", :sex_male true}) "Medipally" "Surender")))
    
    
    ))


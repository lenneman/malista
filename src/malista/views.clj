(ns malista.views
  (:require [malista.db :as db]
            [clojure.string :as str]
            [hiccup.page :as page]
            [ring.util.anti-forgery :as util]
            [clojure.string :as str]))

(defn gen-page-head
  [title]
  [:head
   [:title (str "Match Results Badminton: " title)]
   (page/include-css "/css/styles.css")])

(def header-links
  [:div#header-links
   "[ "
   [:a {:href "/"} "Home"]
   " | "
   [:a {:href "/add-new-player"} "Add a new Player"]
   " | "
   [:a {:href "/all-players"} "List all Players"]
   " | "
   [:a {:href "/add-new-result"} "Add a new Match Result"]
   " | "
   [:a {:href "/all-results"} "List all Match Result"]
   " ]"])

(defn home-page
  []
  (page/html5
   (gen-page-head "Home")
   header-links
   [:h1 "Home"]
   [:p "Webapp to store player and match results and generate statistics about it."]))

(defn add-new-player-page
  []
  (page/html5
   (gen-page-head "Add a new Player")
   header-links
   [:h1 "Add a new Player"]
   [:form {:action "/add-new-player" :method "POST"}
    (util/anti-forgery-field) ; prevents cross-site scripting attacks
    [:p "Firstname: " [:input {:type "text" :name "fname"}]]
    [:p "Surname: "   [:input {:type "text" :name "sname"}]]
    [:p "Gender: "
     [:input {:type "radio" :name "gender" :value "male" :checked ""}] "male"
     [:input {:type "radio" :name "gender" :value "female"}] "female"]
    [:p [:input {:type "submit" :value "submit new player"}]]]))

(defn add-new-player-results-page
  [{:keys [fname sname gender]}]
  (let [id (db/add-new-player-to-db fname sname gender)]
    (page/html5
     (gen-page-head "Added a new Player")
     header-links
     [:h1 "Added a new Player"]
     [:p "Added [" fname ", " sname ", " gender "] (id: " id ") to the db. "])))

(defn all-players-page
  []
  (let [all-players (db/get-all-players)]
    (page/html5
     (gen-page-head "All Players in the db")
     header-links
     [:h1 "All Players"]
     [:table
      [:tr [:th "id"] [:th "surname"] [:th "firstname"] [:th "sex"]]
      (for [player all-players]
        [:tr
         [:td (:playerid player)]
         [:td (:surname player)]
         [:td (:firstname player)]
         [:td (if (:sex_male player)
                "male"
                "female")
          ]])])))

(defn get-datalist-options-with-players
  []
  (let [all-players (db/get-all-players)]
    (for [player all-players]
      [:option {:value (str (:surname player) " " (:firstname player))}])))


(defn add-new-result-page
  []
    (page/html5
     (gen-page-head "Add a new Match Result")
     header-links
     [:h1 "Add a new Match Result"]
     [:form {:action "/add-new-result" :method "POST"}
      (util/anti-forgery-field) ; prevents cross-site scripting attacks
      [:datalist {:id "players"}
       (get-datalist-options-with-players)]
      [:p "Date: "      [:input {:type "date" :name "date"}]]
      [:table
       [:tr
        [:td "Player 1: "
         [:input {:list "players" :name "player1"}]]
        [:td "Opponent 1: "
         [:input {:list "players" :name "opponent1"}]]]
       [:tr
        [:td "Player 2: "
         [:input {:list "players" :name "player2"}]]
        [:td "Opponent 2: "
         [:input {:list "players" :name "opponent2"}]]]
       ]
      [:table
       [:tr
        [:td "Result Set 1: "]
        [:td [:input {:type "text" :name "rs1-p"}]]
        [:td [:input {:type "text" :name "rs1-o"}]]]
       [:tr
        [:td "Result Set 2: "]
        [:td [:input {:type "text" :name "rs2-p"}]]
        [:td [:input {:type "text" :name "rs2-o"}]]]
       [:tr
        [:td "Result Set 3: "]
        [:td [:input {:type "text" :name "rs3-p"}]]
        [:td [:input {:type "text" :name "rs3-o"}]]]]
        
      [:p [:input {:type "submit" :value "submit new result"}]]]))

(defn get-player-id-from-name
  "Determine the player id from the name.
  The parameters `pl-surname` and `pl-firstname` are looked up in the
  parameter `players`. The parameter `players` is a list of maps, the
  maps contain at least the keys `:surname`, `:firstname` and
  `:playerid`. The map is usually the return value of the database
  query done by `db/get-all-players`"
  [players pl-surname pl-firstname]
  (:playerid
   (first
    (filter (fn [playermap]
              (if (and (= (:surname playermap) pl-surname)
                       (= (:firstname playermap) pl-firstname))
                true false)) players))))

(defn add-new-result-results-page
  [{:keys [date player1 player2 opponent1 opponent2 rs1-p rs1-o rs2-p rs2-o rs3-p rs3-o]}]
;;; TODO: implement sanity checks (e.g. player is not doubled in opponent)

                                        ;(let [id (db/add-new-player-to-db fname sname sex)]
  (let [players (db/get-all-players)
        p1-surname (first (str/split player1 #" "))
        p1-fname (second (str/split player1 #" "))
        p2-surname (first (str/split player2 #" "))
        p2-fname (second (str/split player2 #" "))
        o1-surname (first (str/split opponent1 #" "))
        o1-fname (second (str/split opponent1 #" "))
        o2-surname (first (str/split opponent2 #" "))
        o2-fname (second (str/split opponent2 #" "))]
    (page/html5
     (gen-page-head "Added a new Result")
     header-links
     [:h1 "Added a new Result"]
     [:p "Added [" date ]
     [:p player1 "(" (get-player-id-from-name players p1-surname p1-fname) ") "]
     [:p player2 "(" (get-player-id-from-name players p2-surname p2-fname) ") "]
     [:p opponent1 "(" (get-player-id-from-name players o1-surname o1-fname) ") "]
     [:p opponent1 "(" (get-player-id-from-name players o2-surname o2-fname) ")"]
     [:p "results: " rs1-p ":" rs1-o ", " rs2-p ":" rs2-o ", " rs3-p ":" rs3-o " ] to the db."]
     )))


(defn all-results-page
  []
  (let [all-match-results (db/get-all-match-results)]
    (page/html5
     (gen-page-head "All Match Results in the db")
     header-links
     [:h1 "All Match Results"]
     [:table
      [:tr
       [:th "id"]
       [:th "Date"]
       [:th "Player 1"]
       [:th "Player 2"]
       [:th "Opponent 1"]
       [:th "Opponent 2"]
       [:th "Result Set 1"]
       [:th "Result Set 2"]
       [:th "Result Set 3"]]
      (for [mres all-match-results]
        [:tr
         [:td (:match_id    mres)]
         [:td (:player1     mres)]
         [:td (:player2     mres)]
         [:td (:opponent1   mres)]
         [:td (:opponent2   mres)]
         [:td (:result_set1 mres)]
         [:td (:result_set2 mres)]
         [:td (:result_set3 mres)]
          ])])))

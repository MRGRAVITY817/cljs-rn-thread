(ns app.profile
  (:require [react-native :as rn]
            [uix.core :refer [$ defui] :as uix]
            [app.data :refer [users]]))

(defn format-number [n]
  (cond
    (>= n 1000000) (str (Math/floor (/ n 100000) / 10) "M")
    (>= n 1000) (str (Math/floor (/ n 100) / 10) "K")
    :else (str n)))

(defui stat-item [{:keys [count label]}]
  ($ rn/View {:style {:align-items "center"
                      :margin-horizontal 16}}
     ($ rn/Text {:style {:font-size 18
                         :font-weight "bold"
                         :color "#0f1419"}}
        (format-number count))
     ($ rn/Text {:style {:font-size 13
                         :color "#536471"
                         :margin-top 2}}
        label)))

(defui profile-header [{:keys [user on-back]}]
  ($ rn/View {:style {:background-color "white"
                      :border-bottom-width 0.5
                      :border-bottom-color "#e1e8ed"}}

     ;; Header bar with SafeAreaView for proper spacing
     ($ rn/SafeAreaView {:style {:background-color "white"}}
        ($ rn/View {:style {:flex-direction "row"
                            :align-items "center"
                            :padding-horizontal 16
                            :padding-vertical 12}}
           ($ rn/TouchableOpacity {:on-press on-back
                                   :style {:padding 8
                                           :margin-right 12}}
              ($ rn/Text {:style {:font-size 18}} "←"))
           ($ rn/View {:style {:flex 1}}
              ($ rn/Text {:style {:font-size 20
                                  :font-weight "bold"
                                  :color "#0f1419"}}
                 (:name user))
              ($ rn/Text {:style {:font-size 13
                                  :color "#536471"}}
                 (str (format-number (:posts user)) " posts")))))

     ;; Profile info
     ($ rn/View {:style {:padding-horizontal 16
                         :padding-vertical 16}}

        ;; Avatar and follow button
        ($ rn/View {:style {:flex-direction "row"
                            :justify-content "space-between"
                            :align-items "start"
                            :margin-bottom 12}}
           ($ rn/View {:style {:width 80
                               :height 80
                               :border-radius 40
                               :background-color "#e1e8ed"
                               :justify-content "center"
                               :align-items "center"}}
              ($ rn/Text {:style {:font-size 32}} "👤"))

           ($ rn/TouchableOpacity {:style {:border-width 1
                                           :border-color "#cfd9de"
                                           :border-radius 20
                                           :padding-horizontal 16
                                           :padding-vertical 6
                                           :margin-top 8}}
              ($ rn/Text {:style {:color "#0f1419"
                                  :font-weight "600"
                                  :font-size 15}}
                 "Follow")))

        ;; Name and username
        ($ rn/Text {:style {:font-size 20
                            :font-weight "bold"
                            :color "#0f1419"
                            :margin-bottom 4}}
           (:name user))
        ($ rn/Text {:style {:font-size 15
                            :color "#536471"
                            :margin-bottom 12}}
           (:username user))

        ;; Bio
        (when (:bio user)
          ($ rn/Text {:style {:font-size 15
                              :line-height 20
                              :color "#0f1419"
                              :margin-bottom 12}}
             (:bio user)))

        ;; Location and joined date
        ($ rn/View {:style {:flex-direction "row"
                            :align-items "center"
                            :margin-bottom 12}}
           (when (:location user)
             ($ rn/View {:style {:flex-direction "row"
                                 :align-items "center"
                                 :margin-right 16}}
                ($ rn/Text {:style {:font-size 13
                                    :color "#536471"
                                    :margin-right 4}} "📍")
                ($ rn/Text {:style {:font-size 13
                                    :color "#536471"}}
                   (:location user))))

           (when (:joined user)
             ($ rn/View {:style {:flex-direction "row"
                                 :align-items "center"}}
                ($ rn/Text {:style {:font-size 13
                                    :color "#536471"
                                    :margin-right 4}} "📅")
                ($ rn/Text {:style {:font-size 13
                                    :color "#536471"}}
                   (str "Joined " (:joined user))))))

        ;; Stats
        ($ rn/View {:style {:flex-direction "row"
                            :margin-top 12}}
           ($ stat-item {:count (:following user)
                         :label "Following"})
           ($ stat-item {:count (:followers user)
                         :label "Followers"})))))

(defui profile-screen [{:keys [username on-back]}]
  (let [user (get users username)]
    ($ rn/View {:style {:flex 1
                        :background-color "#f7f9fa"}}
       (if user
         ($ profile-header {:user user
                            :on-back on-back})
         ($ rn/View {:style {:flex 1
                             :justify-content "center"
                             :align-items "center"}}
            ($ rn/Text {:style {:font-size 18
                                :color "#536471"}}
               "User not found"))))))
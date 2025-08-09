(ns app.compose
  (:require [react-native :as rn]
            [uix.core :refer [$ defui] :as uix]))

(defui compose-header [{:keys [on-cancel on-post can-post?]}]
  ($ rn/SafeAreaView {:style {:background-color "white"
                              :border-bottom-width 0.5
                              :border-bottom-color "#e1e8ed"}}
     ($ rn/View {:style {:flex-direction "row"
                         :align-items "center"
                         :justify-content "space-between"
                         :padding-horizontal 16
                         :padding-vertical 12}}

        ;; Cancel button
        ($ rn/TouchableOpacity {:on-press on-cancel
                                :style {:padding 8}}
           ($ rn/Text {:style {:color "#536471"
                               :font-size 16}}
              "Cancel"))

        ;; Title
        ($ rn/Text {:style {:font-size 18
                            :font-weight "bold"
                            :color "#0f1419"}}
           "New Post")

        ;; Post button
        ($ rn/TouchableOpacity {:on-press on-post
                                :disabled (not can-post?)
                                :style {:background-color (if can-post? "#1d9bf0" "#8ecdf8")
                                        :border-radius 20
                                        :padding-horizontal 16
                                        :padding-vertical 6}}
           ($ rn/Text {:style {:color "white"
                               :font-weight "bold"
                               :font-size 15}}
              "Post")))))

(defui character-count [{:keys [count max-count]}]
  (let [remaining (- max-count count)
        color (cond
                (< remaining 20) "#f91880" ; Red when close to limit
                (< remaining 50) "#ffad1f" ; Orange warning
                :else "#536471")] ; Gray normal
    ($ rn/View {:style {:align-items "center"
                        :justify-content "center"
                        :margin-top 8}}
       ($ rn/Text {:style {:color color
                           :font-size 13
                           :font-weight "500"}}
          (str remaining " characters remaining")))))

(defui compose-screen [{:keys [on-cancel on-submit]}]
  (let [[post-text set-post-text!] (uix/use-state "")
        max-characters 280
        can-post? (and (> (count (clojure.string/trim post-text)) 0)
                       (<= (count post-text) max-characters))

        handle-post (fn []
                      (when can-post?
                        (on-submit (clojure.string/trim post-text))
                        (set-post-text! "")))]

    ($ rn/View {:style {:flex 1
                        :background-color "#f7f9fa"}}

       ;; Header
       ($ compose-header {:on-cancel on-cancel
                          :on-post handle-post
                          :can-post? can-post?})

       ;; Content area
       ($ rn/View {:style {:flex 1
                           :padding 16}}

          ;; User info (mock current user)
          ($ rn/View {:style {:flex-direction "row"
                              :align-items "start"
                              :margin-bottom 16}}
             ($ rn/View {:style {:width 40
                                 :height 40
                                 :border-radius 20
                                 :background-color "#e1e8ed"
                                 :justify-content "center"
                                 :align-items "center"
                                 :margin-right 12}}
                ($ rn/Text {:style {:font-size 16
                                    :color "#536471"}} "👤"))
             ($ rn/View {:style {:flex 1}}
                ($ rn/Text {:style {:font-size 15
                                    :font-weight "bold"
                                    :color "#0f1419"
                                    :margin-bottom 4}}
                   "You")
                ($ rn/Text {:style {:font-size 13
                                    :color "#536471"}}
                   "@you")))

          ;; Text input
          ($ rn/TextInput {:style {:background-color "white"
                                   :border-radius 12
                                   :padding 16
                                   :font-size 16
                                   :line-height 24
                                   :color "#0f1419"
                                   :border-width 1
                                   :border-color "#e1e8ed"
                                   :min-height 120
                                   :text-align-vertical "top"}
                           :placeholder "What's happening?"
                           :placeholder-text-color "#536471"
                           :multiline true
                           :value post-text
                           :on-change-text set-post-text!
                           :max-length max-characters
                           :auto-focus true})

          ;; Character count
          ($ character-count {:count (count post-text)
                              :max-count max-characters})

          ;; Spacer
          ($ rn/View {:style {:flex 1}})))))
(ns components.reply-composer
  (:require [react-native :as rn]
            [uix.core :refer [$ defui] :as uix]))

(defui reply-header
  "Header for reply composer"
  [{:keys [on-cancel on-reply can-reply? replying-to]}]
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
        ($ rn/View {:style {:align-items "center"}}
           ($ rn/Text {:style {:font-size 18
                               :font-weight "bold"
                               :color "#0f1419"}}
              "Reply")
           (when replying-to
             ($ rn/Text {:style {:font-size 12
                                 :color "#536471"}}
                (str "to " (get-in replying-to [:user :name])))))

        ;; Reply button
        ($ rn/TouchableOpacity {:on-press on-reply
                                :disabled (not can-reply?)
                                :style {:background-color (if can-reply? "#1d9bf0" "#8ecdf8")
                                        :border-radius 20
                                        :padding-horizontal 16
                                        :padding-vertical 6}}
           ($ rn/Text {:style {:color "white"
                               :font-weight "bold"
                               :font-size 15}}
              "Reply")))))

(defui original-post
  "Display the original post being replied to"
  [{:keys [post]}]
  ($ rn/View {:style {:background-color "white"
                      :border-radius 12
                      :margin 16
                      :padding 12
                      :border-width 1
                      :border-color "#e1e8ed"}}

     ;; User info
     ($ rn/View {:style {:flex-direction "row"
                         :align-items "center"
                         :margin-bottom 8}}
        ($ rn/View {:style {:width 32
                            :height 32
                            :border-radius 16
                            :background-color "#e1e8ed"
                            :justify-content "center"
                            :align-items "center"
                            :margin-right 8}}
           ($ rn/Text {:style {:font-size 14}} "👤"))
        ($ rn/View {:style {:flex 1}}
           ($ rn/Text {:style {:font-weight "bold"
                               :color "#0f1419"
                               :font-size 14}}
              (get-in post [:user :name]))
           ($ rn/Text {:style {:color "#536471"
                               :font-size 12
                               :margin-left 4}}
              (get-in post [:user :username]))))

     ;; Post content
     ($ rn/Text {:style {:color "#0f1419"
                         :font-size 14
                         :line-height 18}}
        (:content post))))

(defui character-count
  "Character counter for reply"
  [{:keys [count max-count]}]
  (let [remaining (- max-count count)
        color (cond
                (< remaining 20) "#f91880" ; Red when close to limit
                (< remaining 50) "#ffad1f" ; Orange warning
                :else "#536471")] ; Gray normal
    ($ rn/View {:style {:align-items "center"
                        :padding-vertical 8}}
       ($ rn/Text {:style {:color color
                           :font-size 12
                           :font-weight "500"}}
          (str remaining " characters remaining")))))

(defui reply-composer
  "Main reply composer screen"
  [{:keys [replying-to on-cancel on-submit]}]
  (let [[reply-text set-reply-text!] (uix/use-state "")
        max-characters 280
        can-reply? (and (> (count (clojure.string/trim reply-text)) 0)
                        (<= (count reply-text) max-characters))

        handle-reply (fn []
                       (when can-reply?
                         (on-submit (clojure.string/trim reply-text))
                         (set-reply-text! "")))]

    ($ rn/View {:style {:flex 1
                        :background-color "#f7f9fa"}}

       ;; Header
       ($ reply-header {:on-cancel on-cancel
                        :on-reply handle-reply
                        :can-reply? can-reply?
                        :replying-to replying-to})

       ($ rn/ScrollView {:style {:flex 1}
                         :keyboard-should-persist-taps "handled"}

          ;; Original post
          (when replying-to
            ($ original-post {:post replying-to}))

          ;; Reply composition area
          ($ rn/View {:style {:padding 16}}

             ;; User info (current user)
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
                   ($ rn/Text {:style {:font-size 16}} "👤"))
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
                                      :min-height 100
                                      :text-align-vertical "top"}
                              :placeholder (str "Reply to " (get-in replying-to [:user :name]))
                              :placeholder-text-color "#536471"
                              :multiline true
                              :value reply-text
                              :on-change-text set-reply-text!
                              :max-length max-characters
                              :auto-focus true})

             ;; Character count
             ($ character-count {:count (count reply-text)
                                 :max-count max-characters}))))))
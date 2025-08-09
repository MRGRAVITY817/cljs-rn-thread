(ns components.hashtag-text
  (:require [react-native :as rn]
            [uix.core :refer [$ defui] :as uix]
            [clojure.string :as str]))

(defn parse-hashtags
  "Parse text and identify hashtags, returning segments with type info"
  [text]
  (let [hashtag-pattern #"(#\w+)"
        segments (str/split text hashtag-pattern)]
    (->> segments
         (remove empty?)
         (mapv (fn [segment]
                 (if (re-matches hashtag-pattern segment)
                   {:type :hashtag
                    :text segment
                    :hashtag (str/lower-case segment)}
                   {:type :text
                    :text segment}))))))

(defn parse-mentions
  "Parse text and identify @mentions, returning segments with type info"
  [text]
  (let [mention-pattern #"(@\w+)"
        segments (str/split text mention-pattern)]
    (->> segments
         (remove empty?)
         (mapv (fn [segment]
                 (if (re-matches mention-pattern segment)
                   {:type :mention
                    :text segment
                    :username segment}
                   {:type :text
                    :text segment}))))))

(defn parse-text-content
  "Parse text for hashtags and mentions, returning structured segments"
  [text]
  (let [hashtag-pattern #"(#\w+)"
        mention-pattern #"(@\w+)"
        combined-pattern (re-pattern (str "(" hashtag-pattern "|" mention-pattern ")"))]
    (->> (str/split text combined-pattern)
         (remove empty?)
         (mapv (fn [segment]
                 (cond
                   (re-matches hashtag-pattern segment)
                   {:type :hashtag
                    :text segment
                    :hashtag segment}

                   (re-matches mention-pattern segment)
                   {:type :mention
                    :text segment
                    :username segment}

                   :else
                   {:type :text
                    :text segment}))))))

(defui hashtag-text
  "Render text with clickable hashtags and mentions"
  [{:keys [content style on-hashtag-press on-mention-press]}]
  (let [segments (parse-text-content content)]
    ($ rn/Text {:style style}
       (for [segment segments]
         (case (:type segment)
           :hashtag
           ($ rn/Text {:key (str (:text segment) (rand-int 1000))
                       :style {:color "#1d9bf0"
                               :font-weight "500"}
                       :onPress #(when on-hashtag-press
                                   (on-hashtag-press (:hashtag segment)))}
              (:text segment))

           :mention
           ($ rn/Text {:key (str (:text segment) (rand-int 1000))
                       :style {:color "#1d9bf0"
                               :font-weight "500"}
                       :onPress #(when on-mention-press
                                   (on-mention-press (:username segment)))}
              (:text segment))

           :text
           ($ rn/Text {:key (str (:text segment) (rand-int 1000))}
              (:text segment)))))))
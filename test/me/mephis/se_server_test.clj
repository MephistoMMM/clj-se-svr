(ns me.mephis.se-server-test
  (:require [clojure.test :refer :all]
            [me.mephis.se-server :refer :all]))

(deftest modify-test
  (testing "modify yaml content"
    (let [f (modify-yaml-content "/tmp")]
      (f {:params {:name "se-server-test.yml"}
          :body "{\"data\": \"modifier\"}"})
      (is (= "modifier"
             (slurp "/tmp/se-server-test.yml"))))
    ))

(deftest provide-test
  (testing "provide yaml content"
    (spit "/tmp/se-server-test.yml" "provider")
    (let [f (provide-yaml-content "/tmp")]
      (is (= "provider"
             (f {:params {:name "se-server-test.yml"}}))))
    ))


module SerializationTests (testSerialization)  where
import Test.Tasty.HUnit
import Test.Tasty
import TestCommon
import Article
import Data.Aeson
import Data.Maybe
import Feed
import State
import Cursor

exampleArticle = Article "Hay" "orange.website.com/5" False

exampleFeed = Feed "Orange Website" "orange.website.com/rss" [exampleArticle]

exampleState = State{feeds = [exampleFeed], cursorFeeds = Cursor 1, cursorArticles = Cursor 1}

testSerialization = testGroup "Serialization" [
  roundTripSerializeExampleArticle,
  roundTripSerializeExampleFeed,
  roundTripSerializeExampleState
  ]

roundTripSerializeExampleArticle = 
  testCase 
  "An article can be serialized and correctly deserialized" $ 
  assertThat (exampleArticle == (fromJust $ decode $ encode exampleArticle))

roundTripSerializeExampleFeed = 
  testCase
  "A Feed can be serialized and correctly deserialized" $
  assertThat (exampleFeed == (fromJust $ decode $ encode exampleFeed))
roundTripSerializeExampleState = 
  testCase
  "An Application State can be serialized and deserialized correctly" $
  assertThat (exampleState == (fromJust $ decode $ encode exampleState))

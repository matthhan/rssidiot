module ParsingTests (testParsing)  where
import Test.Tasty.HUnit
import Test.Tasty
import TestCommon
import Parsing
import Article


firstExample = "<item><link>www.foo.de</link><title>HAy</title></item>\
                \<item><link>www.blub.com</link><title>hohoho</title></item>"
firstExampleParsed = getArticles firstExample

testParsing = testGroup "Parsing" [ 
  firstExampleParsesEnoughItems, 
  firstExampleParsesItemsRight ,
  secondExampleJustDoesntGiveItems
  ]

firstExampleParsesEnoughItems = 
  testCase 
  "The Parser parses enough items in the first example" $ 
  assertThat ((length firstExampleParsed) == 2)


firstExampleParsesItemsRight = 
  testCase
  "The Parser parses the items in the first example right" $
  assertThat ((length $ (filter (\x -> title x == "HAy")) firstExampleParsed) == 1)

secondExample = "<item>blergh"
secondExampleParsed = getArticles secondExample

secondExampleJustDoesntGiveItems = 
  testCase 
  "If the result does not contain items, an empty list is returned" $
  assertThat ((length secondExampleParsed) == 0)

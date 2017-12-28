import Test.Tasty
import Test.Tasty.HUnit
import ParsingTests

main = defaultMain tests

assertThat x = assertEqual "" x True

tests :: TestTree
tests = testGroup "Unit Tests" [ testParsing ]

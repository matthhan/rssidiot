module TestCommon (assertThat)  where
import Test.Tasty.HUnit
import Test.Tasty

assertThat x = assertEqual "" x True

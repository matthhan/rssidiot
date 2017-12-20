module Main (main) where
import DataFetching
import Parsing
import Article

main :: IO ()
main = do
  x <- getContentFromUrl "https://news.ycombinator.com/rss" 
  print (getArticles x)



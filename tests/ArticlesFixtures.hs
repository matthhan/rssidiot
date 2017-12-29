module ArticlesFixtures (hackerNewsArticles, redditArticles, fefeArticles)  where
import Article

hackerNewsArticles = [
    Article "Peter Thiel buys Country" "orange.website.com/7" False,
    Article "Rust solves all your problems" "orange.website.com/9" False,
    Article "PageRank actually invented by McCarthy in the 70s" "orange.website.com/999" True
  ]
redditArticles = [
    Article "Look at this little pupper" "reddit.com/577" False,
    Article "This girl made one hell of a hogger at blizzcon" "reddit.com/322" True,
    Article "I'll never see Jack White the same way again" "reddit.com/233" False,
    Article "When the library listens better than the FCC" "reddit.com/895" False
  ]
fefeArticles = [
  Article "Wir werden alle störben" "blog.fefe.de/22" True,
  Article "CCC mindfuck talk" "blog.fefe.de/33" False,
  Article "Vorratsdatenspeicherung nervt" "blog.fefe.de/42" False
  ]

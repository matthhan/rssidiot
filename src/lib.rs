mod article;
mod feed;
use article::Article;

pub fn run() {
    let art = Article::new(String::from("Hello"), String::from("World"));
    println!("{:?}",art);
    let art = art.set_read();
    println!("{:?}",art);
}

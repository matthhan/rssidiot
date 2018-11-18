use application::ApplicationState;
use std::cmp;



pub fn render_state(state:&ApplicationState) -> String{
    let a = get_feed_lines(state);
    let b = get_article_lines(state);
    let res = format_table(a, b);
    res.join("\n")
}
fn get_feed_lines(state: &ApplicationState) -> Vec<String> {
    let mut feed_lines:Vec<String> = Vec::new();
    feed_lines.push("Feeds".to_string());
    for (i, feed) in state.feeds.iter().enumerate() {
        let mut possibly_cursor = "";
        if state.feeds_cursor.position.filter(|&x| x == i).is_some() {
            possibly_cursor = "*"
        }         
        feed_lines.push([possibly_cursor.to_string(),feed.name.clone()].join(""));
    }
    feed_lines
}
fn get_article_lines(state: &ApplicationState) -> Vec<String> {
    let mut article_lines:Vec<String> = Vec::new();
    article_lines.push("Articles".to_string());
    if state.feeds_cursor.position.is_some() {
        let fd = &state.feeds[state.feeds_cursor.position.unwrap()];
        for (i,art) in fd.articles.iter().enumerate() {
            let mut possibly_cursor = "";
            if state.articles_cursor.position.filter(|&x| x ==i).is_some() {
                possibly_cursor = "*"
            }             
            let mut possibly_read = "";
            if art.read {
                possibly_read = "(r)"
            }
            article_lines.push([possibly_cursor.to_string(),art.title.clone(),possibly_read.to_string()].join(""));
        }
    }
    article_lines
}
fn format_table(cola:Vec<String>, colb:Vec<String>) -> Vec<String> {
    let colwidth = 30;
    let mut res:Vec<String> = Vec::new();
    let final_size = cmp::max(cola.len(), colb.len());
    let hrule = "-".to_string().repeat(2*colwidth+3);
    res.push(hrule.clone());
    res.push( ["|".to_string(), right_pad(cola[0].clone(), colwidth), "|".to_string(), right_pad(colb[0].clone(),colwidth), "|".to_string()].join(""));
    res.push(hrule.clone());
    for i in 1..final_size {
        let left = if cola.len() > i { cola[i].clone() } else { "".to_string() };
        let right = if colb.len() > i { colb[i].clone() } else { "".to_string() };
        let lin = ["|".to_string(), right_pad(left, colwidth), "|".to_string(), right_pad(right,colwidth), "|".to_string()].join("");
        res.push(lin)
    }
    res.push(hrule.clone());
    res
}
pub fn right_pad(a: String, siz:usize) -> String {
    let len = a.chars().count();
    let pad_len = siz - len;
    let pd = " ".to_string().repeat(pad_len);
    [a, pd].join("")
}



#[cfg(test)]
mod tests {
    use render_state::render_state;
    use application::ApplicationState;
    #[test]
    fn can_render_a_state() {
        let st = ApplicationState::load_from_file("src/example_state.json".to_string());
        render_state(&st);
    }
}

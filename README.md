# Scala for the impatient 

<a target="_blank" href="https://derlin.github.io/scala-for-the-impatient"><img src="https://img.shields.io/badge/derlin.github.io-scala--for--the--impatient-f66fb2.svg"></a>

I discovered scala during a Master course and was immediately seduced by it. There are many things I love about it: its support for both funtional and OO programming (immutable collections are the default!), the number of different ways to do one thing, the type inference system, the currying and lambda support, the partially applied function syntax, … In short, I like it.

To better understand the language (one course is a bit limited to grasp the whole power of scala), I began reading ["Scala for the impatient"](http://www.horstmann.com/scala/) by Cay Horstmann.  

This repository contains my solution to Horstmann's book. They are far from perfect (given that I just discovered scala and never really used it outside of this repo), but it was very fun to do.

__==> [https://derlin.github.io/scala-for-the-impatient](https://derlin.github.io/scala-for-the-impatient) <==__

## Notes about the sources

I used _Intellij Idea_ along with the scala plugin, with _scala 2.12_. I did many exercises in a scala worksheet, which I found easier for quick testing. If you try to run a worksheet and run into trouble, simply _copy-paste_ the part of the code you want to test into a regular scala file:

```scala
object MyApp extends App {
  // put the code here
  // don't forget to use println() to output the results 
  // to the console
}
```



## gh-pages  

### docco

To generate the html in gh-pages, I used [docco](http://jashkenas.github.io/docco/) with the _parallel_ template with slight changes. You can find the template in the `docco-template` directory. To regenerate the HTML files, use:

```shell
docco -t docco-template/docco.jst -c docco-template/docco.css -o gh-pages src/main/scala/* index.md
```

__Important__: the `.sc` extension is not recognized by docco. Unfortunately, the `-e .scala` option of the docco command-line tool cannot be used, since we also have a `.md` in the source list (for the _jump to_ section to be correct, we need all the sources to be processed at the same time). __Quick fix__: find the `node_modules/docco` directory, open  the `languages.json` file in your favorite editor and add the following to the list:

```json
".sc": {"name": "scala", "symbol": "//"},
```

### git setup

To manage the`master` and the `gh-pages` branches, I followed the tips presented in [this gist](https://gist.github.com/chrisjacob/825950). In short:

1. create a repo with your sources

2. add the name of the folder which will contain the sources of the gh-pages branch to the `.gitignore`

3. commit and push to `origin master`

4. navigate to the _child_ folder (the one for the gh-pages, let's call it `gh-pages`) and 

   1. clone your repo
   2. create  a `gh-pages`branch
   3. remove the `master`branch (don't worry, won't affect the remote)
   4. remove all the files from the master that you don't need in your gh-pages, add files as you need 
   5. commit and push to `origin gh-pages`

   ```shell
   git clone <your repo origin>
   git checkout -b gh-pages
   git branch -d master
   rm <everything but the .git folder>
   git commit -a "first child commit"
   git push origin gh-pages
   ```


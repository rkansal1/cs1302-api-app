# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice-looking HTML.

## Part 1.1: App Description

> Please provide a friendly description of your app, including
> the primary functions available to users of the app. Be sure to
> describe exactly what APIs you are using and how they are connected
> in a meaningful way.

> **Also, include the GitHub `https` URL to your repository.**

Repository: https://github.com/rkansal1/cs1302-api-app

This app allows users to select a New York Times article category. The NYT API is then queried for
    top articles from that category. Each article has a list of keywords. The Art Institute of Chicago (ARTIC) API
    is then automatically queried to search for all artworks matching each of the keywords, assembling a list of
    image urls that are then randomly picked from to create a gallery of 20 ARTIC images that represent the top article of
    the chosen NYT category.

## Part 1.2: APIs

> For each RESTful JSON API that your app uses (at least two are required),
> include an example URL for a typical request made by your app. If you
> need to include additional notes (e.g., regarding API keys or rate
> limits), then you can do that below the URL/URI. Placeholders for this
> information are provided below. If your app uses more than two RESTful
> JSON APIs, then include them with similar formatting.

### New York Times API

```
https://api.nytimes.com/svc/topstories/v2/arts.json?api-key=fnZeEiDLqZ6RLRzv2g8rBbSS7ofmqlZo
```

> NYT API needs a key.

### Art Institute of Chicago API

```
https://api.artic.edu/api/v1/artworks/search?q=terrorism
```

## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

I learned a lot more about debugging, especially debugging with try statements. As I was using more complex
    APIs and it was a much longer process to get the images than with the iTunes API in the last project,
    I faced many different issues and had to debug much more rigorously than I ever have had to. I got much
    better at debugging, as well as using try statements to allow me to debug much more effectively.

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

I would probably find a more meaningful way to parse the NYT API to get more article info, as
    when I was already well into coding the project I felt like the way I was using it with
    the ARTIC api wasn't as interesting as I thought it would be when I started.
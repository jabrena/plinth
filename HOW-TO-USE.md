# How to use a system prompt in your development


![](./documentation/prompts.png)

Using a system prompt in your development is straightforward. If you are using a modern IDE that includes AI features, such as Cursor, open the chat:

![](./documentation/cursor-chat1.png)

and type your own **user prompt**, such as the following example:

```
Improve the pom.xml using the cursor rule @112-java-maven-plugins
```

and drag and drop the system prompt that you need into the chat along with the pom.xml file.

The result should be:

![](./documentation/cursor-chat2.png)

---

Another way to interact with models has recently emerged: using CLI tools. The approach is exactly the same.

Starting from a clean session in [Cursor CLI](https://cursor.com/cli):

![](./documentation/cursor-cli1.png)

Type your user prompt in the text area:

```
Improve the pom.xml using the cursor rule @112-java-maven-plugins
```

![](./documentation/cursor-cli2.png)

and finally select the file to which you want to apply the process:

![](./documentation/cursor-cli3.png)

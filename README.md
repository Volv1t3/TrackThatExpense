<body style="font-family: Consolas, sans-serif; font-weight: normal; font-size: 12pt; color: beige">

<blockquote style="font-style: italic; color: whitesmoke"> 
<blockquote style="font-style: italic; color: whitesmoke; font-size: 9pt; text-align: center"> Hi there! I’m a huge fan of Markdown documents, so apologies in
advanced for structuring this as one
</blockquote>

***

<h3 style="text-align: center; font-size: large"> TrackThatExpense: Single Layout, SharedPreferences-enabled, 
multi-context expense tracking application</h3>

<p style="text-align: center; font-size: medium">
The following implements a basic single layout Android application that manages expenses. It includes a local 
storage using <code>SharedPreferences</code> and multiple menu entries, as well as full menu navigation using 
Fragments to handle UI in a composable manner. 
</p>

***

<div style="display: flex; justify-content: center; align-content: center">

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Android](https://img.shields.io/badge/Android-02569B?style=for-the-badge&logo=android&logoColor=white)

</div>

<blockquote style="font-style: italic; color: whitesmoke">

<h2 style="color: beige; font-size: 14pt">&boxUR; Repository Description &boxUL;  </h2>

<p>The present repository contains the files required for running, testing and modifying the base TrackThatExpense 
application, a Java-Android-based application that helps you track expenses with various views including expense 
input, expenses at a glance view, expense management and category management. Given that it was one of the initial 
projects I developed for Android, it does not use the View Model classes, instead it relies on an on-device memory 
model using <code>Shared Preferences</code>, an approach used given its simplicity of implementation aside from 
serialization and deserialization of JSON data </p>
<p>Internally, the application uses the Fragments model and Fragment View Holders to coordinate UI layouts in a 
composable and plug-and-play manner with inflation of components and controller definitions tied to the fragments 
alone, not to the entire application. On the other hand, it ties serialization and deserialization of data in 
application pauses or exits to make sure that each expense is recorded naturally and without interrupting the flow 
of the app. Each of the Fragments can be visited through a navigable Map defined in the application, which includes 
a custom designed menu item layout that follows the overall application structure and look</p>
<p>The design of the application is solid, as it follows a concise layout for each view, a clear and organized 
layout for buttons, menu items and selectors, as well as a defined color pattern making the application easy to 
follow. It includes icons and guides for each of the application's sections, as well as confirmations for various 
system actions that the user can perform. </p>
<p>The following list defines the folder structure for the application</p>
<ul>

<code>File Structure</code>

<li><b>app</b>: This contains the source code for the application, including both the data required to run it and 
the actual source code required for compiling a usable version of the application. Within this folder, the <code>res,
tests, and java</code> subfolders contain the information pertaining, app resources, tests, and java source code 
respectively.

The application, within the java folder, contains a structured definition separating the respective models from 
fragments and utilities like dialog builders.
</li>

</ul>

</blockquote>

***

<blockquote style="font-style: italic; color: whitesmoke">
<h2 style="color: beige; font-size: 14pt">&boxUR; Methodology &boxUL;  </h2>
<p>The application methodology is straightforward, following best practices for Fragment handling and definition, but 
using simple storage methods and navigation methods. The use of Fragments, SharedPreferences, and navigation methods 
based on menus makes the application unique and powerful, while keeping its architecture lightweight enough to be 
fully understood.
</p>
</blockquote>
</blockquote>

***

</body>

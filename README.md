# UniHousie — Analysis & Design (`main`)

> **You're on the `main` branch.** This branch holds the engineering work behind the product: the requirements analysis, the object-oriented design, and the full UML process.
> The actual Java application lives on the **[`java-impl`](https://github.com/lazourasdim-tech/UniHousie/tree/java-impl)** branch.

## Overview

The project's concept was to analyze, design, implement and develop a software system with no specific professor-driven task. We weren't handed a topic and told "build this." It could have been anything. What pushed us toward UniHousie was thinking critically about a problem we kept seeing around us.

The root cause is this. Student housing in Greece is expensive, and on top of that a lot of students end up living alone and isolated. There's also a communication problem underneath both of these. most students are hesitant to approach each other in person and default to social media and platforms to interact instead, which makes finding a roommate the normal, organic way even harder. These issues usually get treated as separate problems, but they share the same answer, cohabitation. Living with the right roommate makes rent affordable and fixes the isolation at the same time. The issue is that cohabitation isn't really part of the culture here, and there's no good, trustworthy way to find someone you'd actually want to live with. That gap is what UniHousie exists to fill.

So the app connects verified students with compatible roommates, and around that adds the things you need to make it real. browsing and searching listings, scheduling visits, and leaving reviews, all in one place.

This branch is where that idea was turned into a buildable specification. The focus was never on making the app look fancy, it was on getting the functionality, the use cases, and the business logic *right* before a single line of application code was written.

## The process: ICONIX, done as a feedback loop

We followed the **ICONIX** process the whole way through:

```
Use Cases  →  Robustness Diagrams  →  Sequence Diagrams  →  Class Diagram  →  Code
```

The reason this matters, and the reason this branch has *several versions* of almost every diagram, is that ICONIX is not a one-way pipeline where you draw each artifact once and move on. Each stage is a check on the one before it.



## What's in this branch

- **Use case diagram** + the 11 use-case descriptions (UC01–UC11)
- **Domain model** (the final version, post-robustness)
- **11 robustness diagrams** — one per use case, drawn in draw.io
- **11 sequence diagrams** — one per use case, drawn in PlantUML
- **Class diagram** (final version, 51 classes)

## The 11 use cases

| | Use case |
|---|---|
| **UC01** | Student identity verification (university email + gov.gr OTP) |
| **UC02** | Lifestyle profile completion |
| **UC03** | Roommate browsing & selection |
| **UC04** | Mutual match creation |
| **UC05** | In-app messaging |
| **UC06** | Property listing (with Admin approval flow) |
| **UC07** | Property search & viewing |
| **UC08** | Expressing interest in a listing |
| **UC09** | User reporting |
| **UC10** | Property visit scheduling |
| **UC11** | Property & landlord review |

Compatibility between students is scored with Jaccard similarity over lifestyle-habit keywords. A connection only forms when interest is mutual.

## Team

Built by a team of 5, covering requirements analysis, object-oriented design, and the full Java implementation.

| Member | Role | Main contribution |
|---|---|---|
| **Member 1** (me) | Editor / Coordinator | Use cases & robustness for **UC01–UC06**, sequence diagrams, Git workflow, and pulling the separate use cases into one coherent deliverable |
| Member 2 | Maintainer | Project description, Git/repo management, entity & controller implementation |
| Member 3 | Contributor | Use cases & robustness for UC07–UC11, boundary classes |
| Member 4 | Contributor | Domain & class diagram, mockups, code cleanup |
| Member 5 | Reviewer | Domain model, test cases, peer review |

## Tools

- **draw.io** — robustness diagrams
- **PlantUML** — sequence diagrams and the class diagram
- **Git / GitHub** — versioned the whole artifact chain across deliverables

## Reading this branch alongside the code

The whole point of doing it this way is traceability. Any class on the `java-impl` branch traces straight back through a sequence diagram, a robustness diagram, and a use case to an original requirement on this branch. Nothing in the implementation was invented on the spot.

→ **Code:** [`java-impl`](https://github.com/lazourasdim-tech/UniHousie/tree/java-impl)


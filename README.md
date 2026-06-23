# UniHousie — Java Implementation (`java-impl`)

> **You're on the `java-impl` branch.** This branch holds the runnable Java Swing application, under `code/`.
> The analysis & design — the use cases, robustness/sequence/class diagrams, and the story of how they evolved — are on the **[`main`](https://github.com/lazourasdim-tech/UniHousie/tree/main)** branch.

## Overview

This branch is the destination of the process documented on `main`: every use case, robustness diagram, and sequence diagram there was turned into running code here, with nothing added or invented along the way.

The path was deliberately one-directional once the design was locked. The `main` branch's final class diagram became the binding contract: each class here exists because a robustness diagram called for it, and each method exists because a sequence diagram assigned it a message to handle. The Boundary–Control–Entity split isn't a stylistic choice, it's a carry-over of the robustness diagram's own shapes (boundary, control, entity) into Java classes.

One thing worth saying up front: the focus was never on making the app look fancy. The whole point was the functionality, the use cases, and the business idea behind it. The code was driven by what the platform actually needs to do, not by the UI.

## Tech Stack

- **Language:** Java (Swing for the desktop GUI)
- **Runtime:** OpenJDK 21
- **Tooling:** Git / GitHub, IntelliJ IDEA

## Code architecture

The implementation follows a Boundary–Control–Entity (BCE) structure, which maps directly onto the design on the `main` branch. Εvery class here traces back to a robustness diagram, a sequence diagram, and a use case. There's nothing in the code that wasn't specified first.

- **Boundary** — the Swing screens and dialogs the user interacts with (14 classes)
- **Control** — the per-use-case logic that coordinates each flow (13 classes)
- **Entity** — the domain objects that hold state: `Student`, `Landlord`, `Admin`, `HousingListing`, `MutualMatch`, `InterestExpression`, `PropertyVisit`, `Review`, `Report`, `Message`, `LifestyleProfile`, `VerificationAttempt`, and more (15 classes)


## My Contributions

I was the project coordinator (Member 1 / Editor). What I actually did:

- **Feature development:** designed and built **UC01 – Student Verification**, the entry point that locks the platform behind university-email verification.
- **ICONIX analysis & design:** owned the use-case descriptions, robustness diagrams, and sequence diagrams for **UC01–UC06**, and kept them in sync with the code as it was written.
- **Coordination:** kept the team moving through the analysis and design phases and pulled the separate use cases together into one coherent deliverable.
- **Version control:** ran the Git workflow and branching setup (`main` for docs/diagrams, `java-impl` for the Java code), keeping the history clean and everyone's contributions visible.

## How to Run

1. Clone the repo and switch to this branch:
   ```bash
   git clone https://github.com/lazourasdim-tech/UniHousie.git
   cd UniHousie
   git checkout java-impl
   ```

2. Open it in IntelliJ IDEA with OpenJDK 21, or compile from the terminal:
   ```bash
   javac -d out $(find code -name "*.java")
   ```

3. Run it (replace `<MainClass>` with the actual entry-point class) :
   ```bash
   java -cp out <MainClass>
   ```

---

→ **Analysis & design:** [`main`](https://github.com/lazourasdim-tech/UniHousie/tree/main)


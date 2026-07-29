# Implementation Plan - UI Compactness & FAB Cleanup

This plan addresses the oversized cards in the Accounts and Credit Cards screens and the issue where the Floating Action Button (FAB) obscures content at the bottom of lists.

## User Review Required

> [!IMPORTANT]
> **List Padding**: I will add significant bottom padding (approx. 80dp) to all main lists (Transactions, Accounts, Credit Cards). This ensures that even when you scroll to the very end, the last item is pushed up high enough so it isn't blocked by the centered "+" button or the bottom navigation bar.

## Proposed Changes

### 1. UI Compactness

#### [MODIFY] [AccountsScreen.kt](file:///D:/Claude%20Code/Android%20App/app/src/main/java/com/pft/tracker/ui/accounts/AccountsScreen.kt)
*   Integrate the **Delete icon** into the main row of the account card (on the far right).
*   Remove the dedicated bottom row for the delete button to reduce card height.
*   Increase `contentPadding` at the bottom to 80dp.

#### [MODIFY] [CreditCardsScreen.kt](file:///D:/Claude%20Code/Android%20App/app/src/main/java/com/pft/tracker/ui/creditcards/CreditCardsScreen.kt)
*   Move the **Delete icon** to the top-right or integrate it with the card details.
*   Compact the vertical spacing between the name, progress bar, and labels.
*   Increase `contentPadding` at the bottom to 80dp.

#### [MODIFY] [TransactionListScreen.kt](file:///D:/Claude%20Code/Android%20App/app/src/main/java/com/pft/tracker/ui/transactions/TransactionListScreen.kt)
*   Increase `contentPadding` at the bottom of the `LazyColumn` to 80dp to prevent FAB overlap.

### 2. Layout Alignment

#### [MODIFY] [Charts.kt](file:///D:/Claude%20Code/Android%20App/app/src/main/java/com/pft/tracker/ui/common/Charts.kt)
*   Ensure all bars and labels in the Bar Chart are vertically aligned at the base.

## Verification Plan

### Manual Verification
1.  **Height**: Compare Accounts/Cards item height to the Transaction list. They should feel similar in density.
2.  **Scroll**: Scroll to the bottom of all 3 lists. Verify the last item's delete button is clearly visible and clickable above the FAB.
3.  **Visuals**: Verify the overall "beauty" and alignment of the new compact cards.

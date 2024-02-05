package smartersemicolon;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class AddSemicolonMoveCaretNewLineAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            // If no editor is available, we cannot proceed
            return;
        }
        final Document document = editor.getDocument();
        Project project = e.getProject();

        WriteCommandAction.runWriteCommandAction(project, () -> {
            CaretModel caretModel = editor.getCaretModel();
            int currentLine = caretModel.getLogicalPosition().line;
            int lineNum = editor.getCaretModel().getLogicalPosition().line;
            int lineStartOffset = document.getLineStartOffset(lineNum);
            int lineEndOffset = document.getLineEndOffset(lineNum);
            String lineText = document.getText().substring(lineStartOffset, lineEndOffset);

            // Get the current line's text to determine the indentation
            String currentLineText = document.getText().substring(lineStartOffset, lineEndOffset);
            String indentation = getLeadingWhitespace(currentLineText);

            if (!lineText.endsWith(";")) {
                // Insert the semicolon at the end of the line followed by a newline and the same indentation
                document.insertString(lineEndOffset, ";\n" + indentation);
            }

            // Move the caret to the start of the new line, accounting for the indentation
            int nextLineStartOffset = document.getLineStartOffset(currentLine + 1) + indentation.length();
            caretModel.moveToOffset(nextLineStartOffset);
        });
    }

    /**
     * Returns the leading whitespace of the given string.
     *
     * @param text The string to analyze.
     * @return A string containing only the leading whitespace characters.
     */
    private String getLeadingWhitespace(String text) {
        int i = 0;
        while (i < text.length() && Character.isWhitespace(text.charAt(i))) {
            i++;
        }
        return text.substring(0, i);
    }
}

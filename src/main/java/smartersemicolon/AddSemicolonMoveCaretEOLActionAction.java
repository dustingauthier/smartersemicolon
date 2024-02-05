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

public class AddSemicolonMoveCaretEOLActionAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            // If the editor is null, we cannot proceed with the action
            return;
        }
        final Document document = editor.getDocument();
        final Project project = e.getProject();

        if (project == null) {
            // If the project is null, we cannot proceed with the action
            return;
        }

        WriteCommandAction.runWriteCommandAction(project, () -> {
            CaretModel caretModel = editor.getCaretModel();
            int lineNum = caretModel.getLogicalPosition().line;
            int lineStartOffset = document.getLineStartOffset(lineNum);
            int lineEndOffset = document.getLineEndOffset(lineNum);
            String lineText = document.getText().substring(lineStartOffset, lineEndOffset);

            if (!lineText.endsWith(";")) {
                // Insert the semicolon at the end of the line
                document.insertString(lineEndOffset, ";");
            }

            // Update the lineEndOffset since we've just added a semicolon
            lineEndOffset = document.getLineEndOffset(lineNum);

            // Move the caret to the end of the current line, right after the semicolon
            caretModel.moveToOffset(lineEndOffset);
        });
    }
}

package io.eyolas.ide.netbeans;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author eyolas
 */
@ActionID(
        category = "Build",
        id = "io.eyolas.ide.nebeans.RunShellCommand"
)
@ActionRegistration(
        iconBase = "io/eyolas/ide/netbeans/kill.png",
        displayName = "#CTL_KillSpringBoot"
)
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 0),
    @ActionReference(path = "Toolbars/Build", position = 500),
    @ActionReference(path = "Shortcuts", name = "S-F10")
})
@Messages("CTL_KillSpringBoot=Kill spring boot")
public final class RunShellCommand implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Process p = Runtime.getRuntime().exec("pgrep -f spring");
            runKillChildrenForEveryLine(p.getInputStream());
            p.waitFor();
        } catch (IOException | InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private static void runKillChildrenForEveryLine(InputStream is) throws InterruptedException, IOException {
        String pid;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            while ((pid = br.readLine()) != null) {
                // kill all the childeren of the pid
                Runtime.getRuntime().exec("kill -9 " + pid).waitFor();
            }
        }
    }
    
}

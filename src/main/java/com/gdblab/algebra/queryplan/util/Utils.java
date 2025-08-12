package com.gdblab.algebra.queryplan.util;

import java.util.ArrayList;
import java.util.List;

import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.algebra.returncontent.ReturnContent;
import com.gdblab.execution.Context;
import com.gdblab.graph.schema.Path;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_FixedWidth;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

public class Utils {

    public static List<Path> iterToList(final PhysicalOperator physicalOp) {
        List<Path> l = new ArrayList<>();
        while (physicalOp.hasNext()) {
            l.add(physicalOp.next());
        }
        return l;
    }

    public static String getTime(long start, long end) {
        long duration = end - start;
        double durationInSeconds = (double) duration / 1_000_000_000.0;
        return String.format("%.3f", durationInSeconds);
    }

    public static Path NodeLink(Path pathA, Path pathB) {

        if (pathA.isLinkeableByNodeWith(pathB)
                && pathA.getSumOfEdgesWith(pathB) <= Context.getInstance().getMaxPathsLength()) {

            switch (Context.getInstance().getSemantic()) {
                case 2:
                    if (!pathA.isTrailWith(pathB))
                        return null;
                    break;
                case 3:
                    if (!pathA.isAcyclicWith(pathB))
                        return null;
                    break;
                case 4:
                    if (!pathA.isSimplePathWith(pathB))
                        return null;
                    break;
            }

            if (pathA.getQuantityOfNodes() == 1 && pathB.getQuantityOfNodes() == 1) {
                return new Path(pathA.getFirst());
            } else if (pathA.getQuantityOfNodes() == 1) {
                return new Path(pathB);
            } else if (pathB.getQuantityOfNodes() == 1) {
                return new Path(pathA);
            } else {
                return new Path(pathA, pathB);
            }
        }

        return null;
    }

    public static int printAndCountPathsExperimental(PhysicalOperator po) {
        Integer counterLP = 0;

        Integer limitCalculatePaths = Context.getInstance().getLimit();

        while (counterLP <= limitCalculatePaths && po.hasNext()) {
            Path p = po.next();
            counterLP++;
        }

        return counterLP;

    }

    public static int printAndCountPaths(PhysicalOperator po) {
        Integer counterLP = 1;

        ArrayList<ReturnContent> returnContentList = Context.getInstance().getReturnedVariables();
        Integer limitCalculatePaths = Context.getInstance().getLimit();

        AsciiTable table = new AsciiTable();

        List<String> columnNames = returnContentList.stream().map(ReturnContent::getReturnName).toList();

        ArrayList<String> columnNamesWithLength = new ArrayList<>(columnNames);
        columnNamesWithLength.add(0, "#");

        CWC_FixedWidth cwc = new CWC_FixedWidth();
        cwc.add(10);

        for (int i = 0; i < columnNames.size(); i++) {
            cwc.add(30);
        }

        table.getRenderer().setCWC(cwc);

        table.addRule();
        table.addRow(columnNamesWithLength).setTextAlignment(TextAlignment.CENTER);
        table.addRule();

        while (counterLP <= limitCalculatePaths && po.hasNext()) {
            Path p = po.next();

            List<String> row = new ArrayList<>();
            row.add(String.valueOf(counterLP));
            for (ReturnContent returnContent : returnContentList) {
                String content = returnContent.getContent(p);
                row.add(content);
            }
            table.addRow(row).setTextAlignment(TextAlignment.CENTER);
            table.addRule();

            counterLP++;
        }

        // Set center alignment for all columns

        System.out.println();
        System.out.println(table.render());

        return counterLP - 1;
    }

}

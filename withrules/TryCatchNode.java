package withrules;
import java.util.List;

public class TryCatchNode extends Node {
    private BlockNode tryBlock;
    private String exceptionVariable;
    private BlockNode catchBlock;
    private BlockNode finallyBlock;

    public TryCatchNode(BlockNode tryBlock, String exceptionVariable, BlockNode catchBlock, BlockNode finallyBlock) {
        this.tryBlock = tryBlock;
        this.exceptionVariable = exceptionVariable;
        this.catchBlock = catchBlock;
        this.finallyBlock = finallyBlock;
    }

    public BlockNode getTryBlock() { return tryBlock; }
    public String getExceptionVariable() { return exceptionVariable; }
    public BlockNode getCatchBlock() { return catchBlock; }
    public BlockNode getFinallyBlock() { return finallyBlock; }
}

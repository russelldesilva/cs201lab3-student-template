import java.util.ArrayList;
import java.util.List;

public class Q1Test {
     public static void main(String[] args) {

          String result, traversal1, traversal2, traversal3;

          traversal1 = "1-2";
          traversal2 = "2-1-3";
          traversal3 = "1-3-2";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println("Expected Result : Invalid traversals");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "1-2-3";
          traversal2 = "2-1-3";
          traversal3 = "1-3-2";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "1-2-3";
          traversal2 = "3-2-1";
          traversal3 = "2-3-1";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println("Expected Result : Invalid traversals");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "3-1-2-5-4";
          traversal2 = "2-1-4-5-3";
          traversal3 = "1-2-3-4-5";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Preorder, Traversal 2 - Postorder, Traversal 3 - Inorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "10-20-30-40-50";
          traversal2 = "20-10-30-40-50";
          traversal3 = "10-50-40-30-20";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();
     } 

      // write your codes here
     public static String verify(String traversal1, String traversal2, String traversal3){
          String[] inputs = { traversal1, traversal2, traversal3 };
          int[][] nodes = new int[3][];

          // split each string on "-", parse to int; if node counts differ -> invalid
          try {
               for (int i = 0; i < 3; i++) {
                    nodes[i] = parse(inputs[i]);
               }
          } catch (NumberFormatException e) {
               return "Invalid traversals";
          }
          if (nodes[0].length != nodes[1].length || nodes[1].length != nodes[2].length) {
               return "Invalid traversals";
          }

          // inorder candidates: strings that are strictly increasing (numerically)
          for (int in = 0; in < 3; in++) {
               if (!isStrictlyIncreasing(nodes[in])) {
                    continue;
               }

               // for each ordering of the other two as (pre, post)
               int a = (in + 1) % 3;
               int b = (in + 2) % 3;
               int[][] orderings = { { a, b }, { b, a } };
               for (int[] ordering : orderings) {
                    int pre = ordering[0];
                    int post = ordering[1];

                    // skip unless pre.first == post.last
                    if (nodes[pre][0] != nodes[post][nodes[post].length - 1]) {
                         continue;
                    }

                    // insert pre's values into a BST; generate its inorder/preorder/postorder
                    Node root = null;
                    for (int value : nodes[pre]) {
                         root = insert(root, value);
                    }
                    List<Integer> inorder = new ArrayList<>();
                    List<Integer> preorder = new ArrayList<>();
                    List<Integer> postorder = new ArrayList<>();
                    inorder(root, inorder);
                    preorder(root, preorder);
                    postorder(root, postorder);

                    // if all three match the candidates -> return labels in Traversal 1/2/3 order
                    if (matches(inorder, nodes[in]) && matches(preorder, nodes[pre])
                              && matches(postorder, nodes[post])) {
                         String[] labels = new String[3];
                         labels[in] = "Inorder";
                         labels[pre] = "Preorder";
                         labels[post] = "Postorder";
                         return "Traversal 1 - " + labels[0] + ", Traversal 2 - " + labels[1]
                                   + ", Traversal 3 - " + labels[2];
                    }
               }
          }

          // nothing matched
          return "Invalid traversals";
     }

     private static class Node {
          int value;
          Node left, right;

          Node(int value) {
               this.value = value;
          }
     }

     private static int[] parse(String traversal) {
          String[] parts = traversal.split("-");
          int[] result = new int[parts.length];
          for (int i = 0; i < parts.length; i++) {
               result[i] = Integer.parseInt(parts[i].trim());
          }
          return result;
     }

     private static boolean isStrictlyIncreasing(int[] values) {
          for (int i = 1; i < values.length; i++) {
               if (values[i - 1] >= values[i]) {
                    return false;
               }
          }
          return true;
     }

     private static Node insert(Node root, int value) {
          if (root == null) {
               return new Node(value);
          }
          if (value < root.value) {
               root.left = insert(root.left, value);
          } else {
               root.right = insert(root.right, value);
          }
          return root;
     }

     private static void inorder(Node node, List<Integer> out) {
          if (node == null) {
               return;
          }
          inorder(node.left, out);
          out.add(node.value);
          inorder(node.right, out);
     }

     private static void preorder(Node node, List<Integer> out) {
          if (node == null) {
               return;
          }
          out.add(node.value);
          preorder(node.left, out);
          preorder(node.right, out);
     }

     private static void postorder(Node node, List<Integer> out) {
          if (node == null) {
               return;
          }
          postorder(node.left, out);
          postorder(node.right, out);
          out.add(node.value);
     }

     private static boolean matches(List<Integer> generated, int[] expected) {
          if (generated.size() != expected.length) {
               return false;
          }
          for (int i = 0; i < expected.length; i++) {
               if (generated.get(i) != expected[i]) {
                    return false;
               }
          }
          return true;
     }
}